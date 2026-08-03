package tomato.com.tomato.security.filter;

import java.io.IOException;
import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class IpRateLimiterFilter extends OncePerRequestFilter {
    private final StringRedisTemplate redisTemplate;
    private final static int MAX_REQUESTS_PER_MINUTE = 5;
    private static final String[] HEADERS_TO_TRY = {
            "X-Forwared-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED FOR",
            "HTTP_VIA",
            "REMOTE_ADDR"
        };
    private static final String[] PUBLIC_URLS = {
        "/login",
        "/signup",
        "/refresh"
    };
    public IpRateLimiterFilter (StringRedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                String ip = extractIpAddress(request);
                String redisKey = "rate_limit:ip" + ip;
                Long currentRequestCount = redisTemplate.opsForValue().increment(redisKey);
                if (currentRequestCount != null && currentRequestCount == 1){
                    redisTemplate.expire(redisKey, Duration.ofMinutes(1));
                }
                if (currentRequestCount != null && currentRequestCount > MAX_REQUESTS_PER_MINUTE){
                    response.setStatus(429);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("error: Too many request.");
                    return;
                }
        filterChain.doFilter(request, response);
    }
    private String extractIpAddress(HttpServletRequest request) {
        for (String header : HEADERS_TO_TRY) {
            String ip = request.getHeader(header);
            if(ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)){
                if (ip.contains(",")) {
                    ip = ip.split(",")[0].trim();
                    return ip;
                }
                return ip;
            } 
        }
        return request.getRemoteAddr();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        for (String url : PUBLIC_URLS) {
            if (request.getRequestURI().endsWith(url)){
                return false;
            }
        }
        return true;
    }
}