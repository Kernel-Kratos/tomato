package tomato.com.restaurant.service.auth;

import java.time.Instant;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tomato.com.restaurant.exceptions.RefreshTokenException;
import tomato.com.restaurant.exceptions.RefreshTokenNotFoundException;
import tomato.com.restaurant.model.RefreshToken;
import tomato.com.restaurant.model.User;
import tomato.com.restaurant.repository.RefreshTokenRepository;
import tomato.com.restaurant.security.JWT.JwtUtils;
import tomato.com.restaurant.security.user.CustomUserDetails;

@RequiredArgsConstructor
@Service
public class AuthService implements IAuthService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;
    @Override
    public User validateRefreshToken(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findById(UUID.fromString(refreshToken))
                .orElseThrow(() -> new RefreshTokenNotFoundException("refresh token now found"));
        if (!token.isRevoked()){
            if(token.getExpiry().isAfter(Instant.now())){
                return token.getUser();
            }
            else {
                throw new RefreshTokenException("Expired");
            }
        }
        throw new RefreshTokenNotFoundException("Refresh Token Does Not Exists");
        //else case.
    }
    @Override
    public String createJwt (User user) {
        CustomUserDetails userDetail = CustomUserDetails.buildUserDetails(user);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
        return jwtUtils.generateTokenForUser(usernamePasswordAuthenticationToken);   
    }
    public String createJwt (String email, String password) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(password, email);
        return jwtUtils.generateTokenForUser(usernamePasswordAuthenticationToken);
    }

}
