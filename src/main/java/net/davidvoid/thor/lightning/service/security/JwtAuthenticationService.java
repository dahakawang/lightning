package net.davidvoid.thor.lightning.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.davidvoid.thor.lightning.exception.AuthenticationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Created by david on 4/26/16.
 */
@Component
public class JwtAuthenticationService {
    static Log logger = LogFactory.getLog(JwtAuthenticationService.class);

    String secretKey = null;
    
    public JwtAuthenticationService() {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("config/key.hmac");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        try {
            secretKey = reader.readLine();
        } catch (IOException e) {
            logger.fatal("unable to read HMAC key");
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                logger.fatal("failed to close file");
            }
        }
    }

    public Authentication authenticate(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            String username = claims.getBody().getSubject();
            if (username == null) throw new AuthenticationException("no subject present");

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, "");
            return token;

        } catch (JwtException e) {
            throw new AuthenticationException("invalid JWT token", e);
        }
    }
    
    public String getToken(String username) {
        return Jwts.builder().setSubject(username).signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }
}
