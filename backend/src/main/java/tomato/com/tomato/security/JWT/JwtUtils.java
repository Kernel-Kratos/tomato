package tomato.com.tomato.security.JWT;


import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import tomato.com.tomato.security.user.CustomUserDetails;



@Component
public class JwtUtils {
    @Value("${auth.token.jwtSecret}")
    private String jwtSecret;

    @Value("${auth.token.expirationInMills}")
    private int expirationTime;

    public String generateTokenForUser(Authentication authentication) {
        CustomUserDetails principalUser = (CustomUserDetails) authentication.getPrincipal();
        List <String> roles = principalUser.getAuthorities()
                .stream()
                .map(GrantedAuthority :: getAuthority)
                .toList();

        return Jwts.builder()
            .subject(principalUser.getEmail())
            .claim("id", principalUser.getId())
            .claim("roles", roles)
            .issuedAt(new Date())
            .expiration(new Date((new Date()).getTime()+ expirationTime))
            .signWith(key()).compact();
    }

    private SecretKey key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
// here the granted authority removes the spring wrapper SimpleGrantedAuthority when creating the user in CustomUserDetails.It extracts the raw string
//claims is a piece of information asserted about user. here id and roles are directly embedded in JWT in key-value pair which makes it easy
// to parse the user's security context in future.

//Principal represents the currently authenticated user or system. 
    public String getUserNameFromToken(String token){
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload().getSubject();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser()
                    .verifyWith(key())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException| MalformedJwtException | IllegalArgumentException e) {
            throw new JwtException(e.getMessage());
        }
    }


}

