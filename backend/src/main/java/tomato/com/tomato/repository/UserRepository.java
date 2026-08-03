package tomato.com.tomato.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tomato.com.tomato.model.User;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

}
