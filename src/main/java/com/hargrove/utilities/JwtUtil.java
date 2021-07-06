package com.hargrove.utilities;

import com.hargrove.exceptions.FailedAuthenticationException;
import com.hargrove.models.User;

import javax.crypto.SecretKey;

public interface JwtUtil {
    SecretKey getKey();

    String getKeyAsString();

    String issueToken(User user, String issuer);

    User checkBasicAuth(String username, String password) throws FailedAuthenticationException;
}
