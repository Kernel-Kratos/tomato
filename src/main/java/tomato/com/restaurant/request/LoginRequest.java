package tomato.com.restaurant.request;

import lombok.Data;

@Data
public class RestaurantLoginRequest {
    private String firstName;
    private String lastName;
    private int phoneNumber;
    private String email;
    private String password;
}
