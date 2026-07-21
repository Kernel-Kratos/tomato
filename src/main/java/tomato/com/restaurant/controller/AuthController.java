package tomato.com.restaurant.controller;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import org.springframework.security.core.AuthenticationException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tomato.com.restaurant.request.LoginRequest;
import tomato.com.restaurant.response.ApiResponse;
import tomato.com.restaurant.response.JwtResponse;
import tomato.com.restaurant.security.JWT.JwtUtils;
import tomato.com.restaurant.security.user.CustomUserDetails;

@RequiredArgsConstructor
@RestController
@RequestMapping("tomato/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping()
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request){
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateTokenForUser(authentication);
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return ResponseEntity.status(200).body(new ApiResponse("success",new JwtResponse(customUserDetails.getId(), jwt)));    
        } catch (AuthenticationException e) {
            return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
