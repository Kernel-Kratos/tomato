package tomato.com.restaurant.request;

import lombok.Data;

@Data
public class RestaurantRequest {
    private String email;
    private String licenseNo;
    private String name;
    private String address;

}
