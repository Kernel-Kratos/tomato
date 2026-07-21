package tomato.com.restaurant.service.user;


import tomato.com.restaurant.model.Role;
import tomato.com.restaurant.model.User;
import tomato.com.restaurant.request.SignUpRequest;

public interface IUserService {
    User createCustomer(SignUpRequest request);
    Role findByRoleName(String role);

}
