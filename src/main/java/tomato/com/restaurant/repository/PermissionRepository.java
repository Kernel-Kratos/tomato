package tomato.com.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tomato.com.restaurant.model.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

}
