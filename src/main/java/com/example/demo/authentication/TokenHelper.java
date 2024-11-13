package com.example.demo.authentication;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenHelper {
    private final String secretKey = "random5367566B59703ABF1RCD454564=JH6FG0HF9HG=65484=0vhg8ch4gc5h5H4/+45FR0DDH8KGH8JG9GF7FD4899849848KJ0GU9YG545454567890";

    private static final Logger LOGGER = LogManager.getLogger();

    public String generateAccessToken(String userId) {
        if (userId!= null) {
            LOGGER.debug("generateAccessToken()-getting authorities from authentication object");

            LOGGER.debug("generateAccessToken()-Generating access token");
            return Jwts.builder()
                    .setHeaderParam("typ", "JWT")
                    .setHeaderParam("alg", "HS512")
                    .setSubject(userId)
                    .setIssuedAt(new Date())
                    .setNotBefore(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                    .claim("token_type", "access_token")
                    .signWith(SignatureAlgorithm.HS512, secretKey)
                    .compact();
        } else{
            LOGGER.error("generateAccessToken()-Authentication object is null");
            throw new IllegalArgumentException("generateAccessToken()-Authentication cannot be null");
        }
    }

    public boolean validateTokenSignature(String token) {
        if(token != null) {
            LOGGER.debug("validateTokenSignature()-Validating token signature");
            // This will throw an exception if the token is not valid
            Jwts.parser()
                    .setSigningKey(secretKey) // use the same Key to verify
                    .build()
                    .parseClaimsJws(token);
            LOGGER.debug("validateTokenSignature()-Token signature is valid");
            return true;
        } else{
            LOGGER.error("validateTokenSignature()-Token is null");
            throw new IllegalArgumentException("validateTokenSignature()-Token cannot be null");
        }
    }
}
