package tomato.com.tomato.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import tomato.com.tomato.model.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

}
