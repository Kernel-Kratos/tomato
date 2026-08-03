package tomato.com.tomato.service.restaurant;
/*package tomato.com.restaurant.service.restaurant;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tomato.com.restaurant.model.Restaurant;
import tomato.com.restaurant.repository.RestaurantRepository;
import tomato.com.restaurant.request.RestaurantLoginRequest;
import tomato.com.restaurant.request.RestaurantRequest;

@RequiredArgsConstructor
@Service
public class RestaurantService implements IRestaurantService {
    private final RestaurantRepository restaurantRepository;

    @Override
    public Restaurant findRestaurantByLicense(String license) {
        return restaurantRepository.findById(license)
                .orElseThrow(() -> new RuntimeException("Restaurant Not found"));
    }

    @Override
    public Restaurant findRestaurantByPhone(int phoneNumber) {
        return restaurantRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Restaurant Not found"));
    }

    @Override
    public Restaurant findRestaurantByEmail(String email) {
        return restaurantRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Restaurant Not found"));
    }

    /*@Override
    public void signInRestaurant(RestaurantLoginRequest loginRequest) {
        if (loginRequest.getEmail() != null ) {
            Restaurant restaurant = findRestaurantByEmail(loginRequest.getEmail());
            if (restaurant.getPassword() != loginRequest.getPassword()){
                new RuntimeException("Password or Email is wrong");
            }
        }
        else if (loginRequest.getPhoneNumber() != 0) {
            Restaurant restaurant = findRestaurantByPhone(loginRequest.getPhoneNumber());
            if (restaurant.getPassword() != loginRequest.getPassword()){
                new RuntimeException("Password or Email is wrong");
            }
        }
    } 

    @Override
    public void singUpRestaurant(RestaurantLoginRequest singUpRequest) {
        Restaurant restaurant = new Restaurant();
        restaurant.setEmail(singUpRequest.getEmail());
        restaurant.setPassword(singUpRequest.getPassword());
        restaurant.setPhoneNumber(singUpRequest.getPhoneNumber());

        restaurant.setFirstName(singUpRequest.getFirstName());
        restaurant.setLastName(singUpRequest.getLastName());
        restaurantRepository.save(restaurant);
    }

    @Override
    public void createRestaurant(RestaurantRequest request) {
       Restaurant restaurant = findRestaurantByEmail(request.getEmail());
       restaurant.setLicenseNo(request.getLicenseNo());
       restaurant.setName(request.getName());
       restaurant.setAddress(request.getAddress());
       restaurantRepository.save(restaurant);
    }
    //createRestaurant will be a optimistic case i'll add session managment later. These are just basics
} */
