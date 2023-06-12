package com.example.ilmnajot.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {


    private final long expireTime = 365 * 3600 * 1000L;
    private final String secretWord = "mySecretWordIsThisPleaseDoNotTellAnyoneAboutIt";

    public String generateToken(String username) {
        return Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(SignatureAlgorithm.HS512, secretWord)
                .compact();
    }

    public String getUsernameFromToken(String username) {
        try {
            return Jwts
                    .parser()
                    .setSigningKey(secretWord)
                    .parseClaimsJws(username)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }
}
