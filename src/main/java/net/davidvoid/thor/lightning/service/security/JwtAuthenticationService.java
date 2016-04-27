package net.davidvoid.thor.lightning.service.security;

import java.security.Key;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.crypto.MacProvider;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Created by david on 4/26/16.
 */
@Component
public class JwtAuthenticationService {
    Key secretKey = null;

    public Authentication authenticate(String jwtToken) {
        Key secretKey = MacProvider.generateKey();

        Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
        return null;
    }
}
