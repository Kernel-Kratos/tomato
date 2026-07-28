package tomato.com.restaurant.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tomato.com.restaurant.model.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findByPermissionName(String permission);

}
