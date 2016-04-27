package net.davidvoid.thor.lightning.service;

import net.davidvoid.thor.lightning.data.access.UserStore;
import net.davidvoid.thor.lightning.entity.User;
import net.davidvoid.thor.lightning.exception.DuplicateUserException;
import net.davidvoid.thor.lightning.exception.ResourceNotFoundException;
import net.davidvoid.thor.lightning.exception.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Auth {
    @Autowired
    UserStore store = null;

    volatile User user = null;
    
    
    public void authenticate(String token) {
        ensureUserLoaded();

        if (user == null || !token.equals(this.user.getPassword())) {
            throw new AuthenticationException("incorrect credential provided");
        }
    }
    
    synchronized public void register(String name, String password) {
        ensureUserLoaded();
        if (user != null) throw new DuplicateUserException("the user already registered");
        
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        store.add(user);
        this.user = user;
    }

    public User getUser() {
        ensureUserLoaded();
        return user;
    }

    private void ensureUserLoaded() {
        // double checked locking with volatile variable
        if (user == null) {
            synchronized(this) {
                if (user == null) {
                    user = retrieve_token();
                }
            }
            
        }
    }

    private User retrieve_token() {
        try {
            User user = store.getUser();
            return user;
        } catch (ResourceNotFoundException e) {
        }
        
        assert false : "should never reach here";
        return null;
    }
}
