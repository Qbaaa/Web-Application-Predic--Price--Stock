package com.qbaaa.stockpricepredict.security.jwt;

import com.qbaaa.stockpricepredict.security.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${JwtUtils.jwtSecret}")
    private String jwtSecret;

    @Value("${JwtUtils.jwtExpiration}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication)
    {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder().setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Nieprawidłowy podpis JWT: ", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Nieprawidłowy  Token JWT: ", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Token JWT wygasł: ", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Token JWT nie jest obsługiwany: ", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Ciag znaków JWT jest pusty: ", e.getMessage());
        }

        return false;
    }

}
