package tomato.com.tomato.service.refreshToken;

import tomato.com.tomato.model.RefreshToken;
import tomato.com.tomato.model.User;

public interface IRefreshToken {

    RefreshToken findByTokenById(String token);

    RefreshToken createRefreshToken(User user);

}
