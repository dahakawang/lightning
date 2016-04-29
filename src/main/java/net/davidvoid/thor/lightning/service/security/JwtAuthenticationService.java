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
import java.util.Date;
import java.util.Map;

import net.davidvoid.thor.lightning.entity.Entity;
import net.davidvoid.thor.lightning.entity.User;
import net.davidvoid.thor.lightning.exception.AuthenticationException;
import net.davidvoid.thor.lightning.util.MapLiteral;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Created by david on 4/26/16.
 */
@Component
public class JwtAuthenticationService {
    static Log logger = LogFactory.getLog(JwtAuthenticationService.class);
    static long EXPIRATION_TIME = 30L * 24L * 60L * 60L * 1000L;

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
            if (username == null) throw new AuthenticationException("no subject field present");
            
            Date issuedDate = claims.getBody().getIssuedAt();
            if (issuedDate == claims) throw new AuthenticationException("no issued at field present");
            Date current = new Date();
            long offset = current.getTime() - issuedDate.getTime();
            if (offset < 0 || offset > EXPIRATION_TIME) throw new AuthenticationException("token expired");
            
            Object uid_obj = claims.getBody().get("uid");
            if (uid_obj == null || !Entity.is_valid_id(uid_obj)) throw new AuthenticationException("no uid field present");

            return new ThorAuthentication(username, uid_obj);

        } catch (JwtException e) {
            throw new AuthenticationException("invalid JWT token", e);
        }
    }

    public String getToken(User user) {
        Map<String, Object> additional_claims = MapLiteral.map("uid", user.getId());

        return Jwts.builder().setClaims(additional_claims).setSubject(user.getName()).setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }
}
