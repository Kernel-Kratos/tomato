package tomato.com.restaurant.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import tomato.com.restaurant.model.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

}
