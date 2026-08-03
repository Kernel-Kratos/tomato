package tomato.com.tomato.service.refreshToken;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tomato.com.tomato.exceptions.ResourceNotFoundException;
import tomato.com.tomato.model.RefreshToken;
import tomato.com.tomato.model.User;
import tomato.com.tomato.repository.RefreshTokenRepository;

@RequiredArgsConstructor
@Service
public class RefreshTokenService implements IRefreshToken {
    private final RefreshTokenRepository refreshTokenRepository;
    @Override
    public RefreshToken findByTokenById(String token) {
        return refreshTokenRepository.findById(UUID.fromString(token))
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));
    }
    @Override 
    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken =  new RefreshToken();
        refreshToken.setCreated(Instant.now());
        refreshToken.setExpiry(Instant.now().plusSeconds(604800));
        refreshToken.setUser(user);
        refreshToken.setRevoked(false);
        return refreshTokenRepository.save(refreshToken);
    }
}
