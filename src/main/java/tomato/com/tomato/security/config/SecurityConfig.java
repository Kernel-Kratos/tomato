package tomato.com.tomato.security.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;
import tomato.com.tomato.RestaurantApplication;
import tomato.com.tomato.security.JWT.JwtAuthenticationEntryPoint;
import tomato.com.tomato.security.filter.AuthTokenFilter;
import tomato.com.tomato.security.filter.IpRateLimiterFilter;
import tomato.com.tomato.security.filter.JwtRateLimitFilter;
import tomato.com.tomato.security.user.CustomUserDetailsService;

@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationEntryPoint authEntryPoint;

    private static final List<String> SECURED_URLS = List.of("/tomato/hello");

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthTokenFilter authTokenFilter() {
        return new AuthTokenFilter();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) {
        return authConfig.getAuthenticationManager();
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        var authProvider = new DaoAuthenticationProvider(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    @Autowired
    public StringRedisTemplate redisTemplate;
    @Bean
    public SecurityFilterChain filterChain (HttpSecurity httpSecurity)  {
        httpSecurity.csrf(AbstractHttpConfigurer :: disable)
            .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPoint))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth.requestMatchers(SECURED_URLS.toArray(String [] :: new)).authenticated()
            .anyRequest().permitAll());
            httpSecurity.authenticationProvider(daoAuthenticationProvider());
            httpSecurity.addFilterBefore(new IpRateLimiterFilter(redisTemplate), LogoutFilter.class);
            httpSecurity.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
            httpSecurity.addFilterAfter(new JwtRateLimitFilter(redisTemplate), AuthTokenFilter.class);
        return httpSecurity.build();
    }
    
    @Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setAllowedMethods(List.of("GET", "POST", "OPTIONS", "PUT", "DELETE"));
		configuration.setAllowedOrigins(List.of("http://localhost:5500", "http://127.0.0.1.5500"));
		configuration.setAllowCredentials(false);
		configuration.setMaxAge(360000L);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
} 


//ByCrpt PasswordEncoder
//returning a new instance of this type and wrapping up it in @bean because there multiple ways to encode the password and by doing 
// this it tells spring to give password Encoder of this type.

//AuthenicationMangaer
//Spring automatically configures and manages authenticationmangaer but keeps it inside framework due to security reasons but since 
// i need this to process user crendentials at login controller i have to extract it from authenticationConfiguration and declare it as bean

//DaoAuthenticationProvider
//Dao - database accesss object. It is used to talk to a db.
//AuthenticationManager is a manager and it delegates its task to many other methods. One of them is DaoAuthenticationProvider(DAP)
//DAP gets customUserDetailsService so it knows to fetch the user from db
// DAP also gets PasswordEncoder so it knows to compare the password given by user against stored hashPassword.  

//Spring filter chain behaviour
// Spring has 3 inbuilt filter chain: csrffilter, logoutfilter and usernamepasswordfilter and assigns them a strict priority.
// so i tried to use my custom filter class(authtokenfilter()) for ipbasefilter but spring threw exception because it was not
// defined or build yet so spring could not use it as anchor.
// but it gets build at line 71 so spring can use it afterwards as it has assigned it order.  
// my ipbased filter has to be built at very start i have to use Spring's filter and esp logout.
// as priority (lowest-highest) : usernamepasswordfilter -> logoutfilter -> csrffilter 