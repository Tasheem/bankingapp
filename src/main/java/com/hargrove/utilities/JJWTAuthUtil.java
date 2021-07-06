package com.hargrove.utilities;

import com.hargrove.exceptions.FailedAuthenticationException;
import com.hargrove.models.User;
import com.hargrove.services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class JJWTAuthUtil implements JwtUtil {
    private SecretKey key;
    private String secretKey;
    private String secretString = "SomeLongSecretKeyIdontKnowWhatToComeUpWithThisIsGoingToBeMyKey";

    public JJWTAuthUtil() {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString));
        secretKey = Encoders.BASE64.encode(key.getEncoded());
    }

    public SecretKey getKey() {
        return key;
    }

    public String getKeyAsString() {
        return secretKey;
    }

    public String issueToken(User user, String issuer) {
        Date expiration = new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(30));

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setHeaderParam("id", UUID.randomUUID())
                .claim("id", user.getID().toString())
                .claim("role", user.getRole())
                .setIssuer(issuer)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(this.key)
                .compact();
    }

    public User checkBasicAuth(String username, String password) throws FailedAuthenticationException {
        UserService userService = new UserService();
        User user = userService.getUser(username, password);

        if(user == null)
            throw new FailedAuthenticationException();

        return user;
    }
}
