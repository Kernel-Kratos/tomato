package tomato.com.tomato.controller;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import tomato.com.tomato.dto.UserDto;
import tomato.com.tomato.exceptions.AlreadyExistsException;
import tomato.com.tomato.model.User;
import tomato.com.tomato.request.SignUpRequest;
import tomato.com.tomato.response.ApiResponse;
import tomato.com.tomato.service.user.IUserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("tomato/signup")
public class UserController {
    private final IUserService userService;
    @PostMapping("/customer")
    public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody SignUpRequest request){
        try{
            User user = userService.createCustomer(request);
        UserDto userDto = new UserDto();
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setId(user.getId());
        userDto.setRoles(user.getRoles().stream().map(role -> role.getRoleName()).collect(Collectors.toSet()));
        return ResponseEntity.status(201).body(new ApiResponse(userDto)); 
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(409).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
