package tomato.com.tomato.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tomato.com.tomato.model.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, String> {

    Optional<Restaurant> findByPhoneNumber(int phoneNumber);

    Optional<Restaurant> findByEmail(String email);

}
