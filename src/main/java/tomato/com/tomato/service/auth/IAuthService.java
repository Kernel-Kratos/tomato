package tomato.com.tomato.service.auth;

import tomato.com.tomato.model.User;

public interface IAuthService {
    User validateRefreshToken(String refreshToken);

    String createJwt(User user);
}
