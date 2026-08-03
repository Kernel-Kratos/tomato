package tomato.com.tomato.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tomato.com.tomato.model.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findByPermissionName(String permission);

}
