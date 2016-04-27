package net.davidvoid.thor.lightning.service.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import net.davidvoid.thor.lightning.exception.AuthenticationException;

import org.junit.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class JwtAuthenticationServiceTest {

    @Test
    public void Authenticate_GivenInvalidToken_WillThrow() {
        JwtAuthenticationService service = new JwtAuthenticationService();
        
        try {
            service.authenticate("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ");
            fail("should throw");
        } catch (AuthenticationException e) {}
    }
    @Test
    public void Authenticate_GivenValid_WillSuccess() {
        JwtAuthenticationService service = new JwtAuthenticationService();
        String jwtToken = service.getToken("david");
        Authentication auth = service.authenticate(jwtToken);
        assertEquals("david", auth.getName());
        assertEquals("david", auth.getPrincipal());
        assertEquals("", auth.getCredentials());
        assertEquals(1, auth.getAuthorities().size());
        GrantedAuthority role = (GrantedAuthority) auth.getAuthorities().toArray()[0];
        assertEquals("USER", role.toString());
        assertEquals(true, auth.isAuthenticated());
    }

}
