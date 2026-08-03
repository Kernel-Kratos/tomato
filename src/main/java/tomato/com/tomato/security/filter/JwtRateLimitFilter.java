package tomato.com.tomato.security.filter;

import java.io.IOException;
import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tomato.com.tomato.security.user.CustomUserDetails;

public class JwtRateLimitFilter extends OncePerRequestFilter{
    private final StringRedisTemplate redisTemplate;
    private final static int MAX_REQUESTS_PER_MINUTE = 5;

    public JwtRateLimitFilter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymous")){
            filterChain.doFilter(request, response);
            return;
        }

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = customUserDetails.getId();

        String redisKey = "rate_limit:user:" + userId;
        Long currentRequestCount =  redisTemplate.opsForValue().increment(redisKey);
        if (currentRequestCount != null && currentRequestCount == 1) {
            redisTemplate.expire(redisKey, Duration.ofMinutes(1));
        }
        if (currentRequestCount != null && currentRequestCount > MAX_REQUESTS_PER_MINUTE){
            response.setStatus(429);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\": \"Too many requests. Please wait a minute\"}");
            return;
        }
        filterChain.doFilter(request, response);
    }

    //ToDo:
    //implemented ratelimit filter for authenticated user.

    //case: if user has been rate limited but tries send a request even then and just spams what to do?
}
