package com.aaroncarlson.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Following utility class will be used for:
 * - generating a JWT after a user logs in successfully
 * - validating JWT token sent in Authorization header of the request
 */
@Component
public class JwtTokenProvider {

    private static Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    // Read from application.yml file
    @Value("${app.jwtSecret}")
    private String jwtSecret;
    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    public String generateToken(final Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public Long getUserIdFromJwt(final String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(final String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException exception) {
            logger.error("Invalid JWT Signature");
        } catch (MalformedJwtException exception) {
            logger.error("Invalid JWT Token");
        } catch (ExpiredJwtException exception) {
            logger.error("Expired JWT Token");
        } catch (UnsupportedJwtException exception) {
            logger.error("Unsupported JWT Token");
        } catch (IllegalArgumentException exception) {
            logger.error("JWT Claims String is Empty");
        }

        return false;
    }

}
