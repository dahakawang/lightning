package net.davidvoid.thor.lightning.service.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class ThorAuthentication extends AbstractAuthenticationToken {

    private static final long serialVersionUID = -5044029477635986803L;

    private String username = null;
    private Object id = -1;

    public ThorAuthentication(String username, Object id) {
        super(defaultRoles());
        setAuthenticated(true);
        this.username = username;
        this.id = id;
    }

    private static List<GrantedAuthority> defaultRoles() {
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("USER"));
        return roles;
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public Object getPrincipal() {
        return username;
    }
    
    public Object getId() {
        return id;
    }

}
