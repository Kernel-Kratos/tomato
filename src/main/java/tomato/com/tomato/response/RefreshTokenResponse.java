package tomato.com.tomato.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenResponse {
    private String token;
    private Instant created;
    private Instant expiry;
}
