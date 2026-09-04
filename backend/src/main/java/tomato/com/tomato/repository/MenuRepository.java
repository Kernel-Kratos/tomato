package tomato.com.tomato.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tomato.com.tomato.model.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long>  {

}
