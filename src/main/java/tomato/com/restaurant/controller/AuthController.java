package tomato.com.restaurant.controller;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_CONTENT;

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
import tomato.com.restaurant.exceptions.RefreshTokenException;
import tomato.com.restaurant.exceptions.ResourceNotFoundException;
import tomato.com.restaurant.model.RefreshToken;
import tomato.com.restaurant.model.User;
import tomato.com.restaurant.request.LoginRequest;
import tomato.com.restaurant.request.RefreshTokenRequest;
import tomato.com.restaurant.response.ApiResponse;
import tomato.com.restaurant.response.JwtResponse;
import tomato.com.restaurant.response.RefreshTokenResponse;
import tomato.com.restaurant.security.JWT.JwtUtils;
import tomato.com.restaurant.security.user.CustomUserDetails;
import tomato.com.restaurant.service.auth.IAuthService;
import tomato.com.restaurant.service.refreshToken.RefreshTokenService;
import tomato.com.restaurant.service.user.IUserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("tomato/auth")
public class AuthController {
    private final RefreshTokenService refreshTokenService;
    private final IUserService userService;
    private final IAuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request){
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateTokenForUser(authentication);
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userService.findUserByEmail(request.getEmail()));
            RefreshTokenResponse refreshTokenResponse = new RefreshTokenResponse(refreshToken.getId().toString(), refreshToken.getCreated(), refreshToken.getExpiry());
            return ResponseEntity.status(200).body(new ApiResponse("success",new JwtResponse(customUserDetails.getId(), jwt, refreshTokenResponse)));    
        } catch (AuthenticationException e) {
            return ResponseEntity.status(UNPROCESSABLE_CONTENT).body(new ApiResponse(e.getLocalizedMessage(), null));
        }
    }
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse> refresh(@Valid @RequestBody RefreshTokenRequest refresh) {
        try{
            System.out.println(refresh.getRefreshToken());
            User user = authService.validateRefreshToken(refresh.getRefreshToken());
            String jwt = authService.createJwt(user);
            return ResponseEntity.status(200).body(new ApiResponse(jwt));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponse("null", "null"));
        } catch (RefreshTokenException e) {
            return ResponseEntity.status(401).body(new ApiResponse("Token expired. Relogin", null));
        }
    }
}
