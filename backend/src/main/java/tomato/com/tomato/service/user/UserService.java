package tomato.com.tomato.service.user;

import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tomato.com.tomato.data.RoleConstants;
import tomato.com.tomato.exceptions.AlreadyExistsException;
import tomato.com.tomato.exceptions.ResourceNotFoundException;
import tomato.com.tomato.model.Role;
import tomato.com.tomato.model.User;
import tomato.com.tomato.repository.RoleRepository;
import tomato.com.tomato.repository.UserRepository;
import tomato.com.tomato.request.SignUpRequest;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    
    public User findUserByEmail(String email){
        User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("User Not found"));
        return user;
    }
    @Override
    public User createUser(SignUpRequest request) {
        if(!userRepository.existsByEmail(request.getEmail())) {
            User newUser = new User();
            newUser.setFirstName(request.getFirstName());
            newUser.setLastName(request.getLastName());
            newUser.setEmail(request.getEmail());
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));
            switch (request.getRole().toLowerCase()) {
                case "restaurant-owner":
                    newUser.setRoles(Set.of(findByRoleName(RoleConstants.owner)));
                    break;
                case "customer":
                    newUser.setRoles(Set.of(findByRoleName(RoleConstants.customer)));
                    break;
                default:
                    newUser.setRoles(Set.of(findByRoleName(RoleConstants.customer)));
                    break;
            }
            return userRepository.save(newUser);
        }
        throw new AlreadyExistsException("User already exists");
    }
    
    @Override
    public Role findByRoleName(String roleName) {
         Role role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new RuntimeException("Not Found"));
        return role;
    }

}
