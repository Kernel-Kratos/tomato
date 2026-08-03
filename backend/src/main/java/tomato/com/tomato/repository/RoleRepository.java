package tomato.com.tomato.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tomato.com.tomato.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(String role);

}
