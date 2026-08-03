package tomato.com.tomato.service.user;

import java.util.Optional;
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
    public User createCustomer(SignUpRequest request) {
        return  Optional.of(request)
                .filter(req -> !userRepository.existsByEmail(request.getEmail()))
                .map(req -> {
                    User newUser = new User();
                    newUser.setFirstName(request.getFirstName());
                    newUser.setLastName(request.getLastName());
                    newUser.setEmail(request.getEmail());
                    newUser.setPassword(passwordEncoder.encode(request.getPassword()));
                    newUser.setRoles(Set.of(findByRoleName(RoleConstants.customer)));
                    return userRepository.save(newUser);
                })
                .orElseThrow(() -> new AlreadyExistsException("User Already Exists. Please login in."));
    }
    @Override
    public Role findByRoleName(String roleName) {
         Role role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new RuntimeException("Not Found"));
        return role;
    }

}
