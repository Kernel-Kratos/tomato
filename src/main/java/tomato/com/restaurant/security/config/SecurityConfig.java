package tomato.com.restaurant.security.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import tomato.com.restaurant.security.JWT.AuthTokenFilter;
import tomato.com.restaurant.security.JWT.JwtAuthenticationEntryPoint;
import tomato.com.restaurant.security.filter.RateLimitFilter;
import tomato.com.restaurant.security.user.CustomUserDetailsService;

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
            .authorizeHttpRequests(auth -> auth.requestMatchers(SECURED_URLS.toArray(String [] :: new)).authenticated()
            .anyRequest().permitAll());
            httpSecurity.authenticationProvider(daoAuthenticationProvider());
            httpSecurity.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
            httpSecurity.addFilterAfter(new RateLimitFilter(redisTemplate), AuthTokenFilter.class);
        return httpSecurity.build();
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