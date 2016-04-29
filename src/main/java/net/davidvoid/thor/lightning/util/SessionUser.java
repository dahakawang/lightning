package net.davidvoid.thor.lightning.util;


import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.davidvoid.thor.lightning.service.security.ThorAuthentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class SessionUser {
    private Authentication authentication = null;
    private boolean anonymous = true;

    public static SessionUser get() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        notNull(auth);

        return new SessionUser(auth);
    }
    
    public List<String> getRoles() {
        Collection<? extends GrantedAuthority> it = authentication.getAuthorities();
        String[] roles = (String[]) it.stream().map((GrantedAuthority a) -> { return a.toString();}).toArray();
        return Arrays.asList(roles);
    }
    
    public String getName() {
        return authentication.getName();
    }
    
    public boolean isAnonymous() {
        return anonymous;
    }
    
    public Object getId() {
        isTrue(!isAnonymous(), "getid can only be called on non-anonymous users");

        return ((ThorAuthentication) authentication).getId();
    }
    
    private SessionUser(Authentication auth) {
        if (auth instanceof ThorAuthentication) {
            anonymous = false;
        } else {
            anonymous = true;
        }

        this.authentication = auth;
    }
}
