package tomato.com.restaurant.service.auth;

import tomato.com.restaurant.model.User;

public interface IAuthService {
    User validateRefreshToken(String refreshToken);

    String createJwt(User user);
}
