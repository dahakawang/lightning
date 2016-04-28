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

    public User authenticate(String username, String password) {
        User user = retrieve_user();

        if (user == null || !username.equals(user.getName())
                || !password.equals(user.getPassword())) {
            throw new AuthenticationException("failed to authenticate user");
        }
        
        return user;
    }
    
    synchronized public User register(String name, String password) {
        User user = retrieve_user();
        if (user != null) throw new DuplicateUserException("the user already registered");
        
        user = new User();
        user.setName(name);
        user.setPassword(password);
        store.add(user);
        
        return user;
    }

    public User getUser(String username) {
        User user = retrieve_user();
        if (user == null || !user.getName().equals(username)) throw new ResourceNotFoundException("the user dose not exists");
        
        return user;
    }

    private User retrieve_user() {
        try {
            User user = store.getUser();
            return user;
        } catch (ResourceNotFoundException e) {
        }
        
        assert false : "should never reach here";
        return null;
    }
}
