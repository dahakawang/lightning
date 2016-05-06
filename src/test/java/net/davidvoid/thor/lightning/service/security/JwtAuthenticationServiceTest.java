package net.davidvoid.thor.lightning.service.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import net.davidvoid.thor.lightning.config.RootConfig;
import net.davidvoid.thor.lightning.entity.User;
import net.davidvoid.thor.lightning.exception.AuthenticationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = { RootConfig.class })
public class JwtAuthenticationServiceTest {

    @Autowired
    JwtAuthenticationService service = new JwtAuthenticationService();

    @Test
    public void getToken_willReturnAValidToken_WichContainsSubjectAndUidAndIssueAtFields() {
        User user = new User();
        user.setName("david");
        user.setPassword("1234");
        user.setId(123);

        String token = service.getToken(user);
        String key = load_key("config/key.hmac");

        Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
        assertEquals("david", claims.getBody().getSubject());
        assertEquals(123, claims.getBody().get("uid"));
        // the issue time should be no more than 5 seconds before now
        assertTrue((new Date().getTime() - claims.getBody().getIssuedAt().getTime()) < 5000);
    }

    public static String load_key(String filename) {
        ClassLoader loader = JwtAuthenticationServiceTest.class.getClassLoader();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(loader.getResourceAsStream(filename)))) {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException("failed to load key");
        }
    }

    @Test
    public void Authenticate_GivenInvalidToken_WillThrow() {
        try {
            service.authenticate("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ");
            fail("should throw");
        } catch (AuthenticationException e) {}
    }

    @Test
    public void Authenticate_GivenValid_WillSuccess() {
        User user = new User();
        user.setName("david");
        user.setId(1);

        String jwtToken = service.getToken(user);
        ThorAuthentication auth = (ThorAuthentication) service.authenticate(jwtToken);
        assertEquals("david", auth.getName());
        assertEquals("david", auth.getPrincipal());
        assertEquals("", auth.getCredentials());
        assertEquals(1, auth.getAuthorities().size());
        GrantedAuthority role = (GrantedAuthority) auth.getAuthorities().toArray()[0];
        assertEquals("USER", role.toString());
        assertEquals(true, auth.isAuthenticated());
        assertEquals(1, auth.getId());
    }

}
