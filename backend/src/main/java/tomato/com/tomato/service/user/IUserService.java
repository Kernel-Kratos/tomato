package tomato.com.tomato.service.user;


import tomato.com.tomato.model.Role;
import tomato.com.tomato.model.User;
import tomato.com.tomato.request.SignUpRequest;

public interface IUserService {
    User createUser(SignUpRequest request);
    Role findByRoleName(String role);
    User findUserByEmail(String email);

}
