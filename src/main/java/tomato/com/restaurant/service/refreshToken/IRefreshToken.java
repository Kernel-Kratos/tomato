package tomato.com.restaurant.service.refreshToken;

import tomato.com.restaurant.model.RefreshToken;
import tomato.com.restaurant.model.User;

public interface IRefreshToken {

    RefreshToken findByTokenById(String token);

    RefreshToken createRefreshToken(User user);

}
