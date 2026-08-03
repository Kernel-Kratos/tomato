package tomato.com.tomato.dto;

import java.util.Collection;
import java.util.HashSet;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String FirstName;
    private String LastName;
    private Collection<String> roles = new HashSet<>();
}
