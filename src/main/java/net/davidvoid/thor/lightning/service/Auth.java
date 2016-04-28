package net.davidvoid.thor.lightning.service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.davidvoid.thor.lightning.data.access.UserStore;
import net.davidvoid.thor.lightning.entity.User;
import net.davidvoid.thor.lightning.exception.AuthenticationException;
import net.davidvoid.thor.lightning.exception.DuplicateUserException;
import net.davidvoid.thor.lightning.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Auth {
    @Autowired
    UserStore store = null;

    public User authenticate(String username, String password) {
        User user = retrieve_user();

        if (user == null || !username.equals(user.getName())
                || !user.getPassword().equals(digest(password))) {
            throw new AuthenticationException("failed to authenticate user");
        }
        
        return user;
    }
    
    public User update(String username, String password) {
        User user = retrieve_user();
        if (user == null || !user.getName().equals(username)) throw new ResourceNotFoundException("user dose not exists");

        user.setPassword(digest(password));
        store.update(user);
        return user;
    }

    public User register(String name, String password) {
        User user = retrieve_user();
        if (user != null) throw new DuplicateUserException("the user already registered");

        user = new User();
        user.setName(name);
        user.setPassword(digest(password));
        store.add(user);
        return user;
    }

    public User getUser(String username) {
        User user = retrieve_user();
        if (user == null || !user.getName().equals(username)) throw new ResourceNotFoundException("the user dose not exists");
        
        return user;
    }
    
    
    private String digest(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            
            return String.format("%064x",  new BigInteger(1, md.digest()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not supported");
        }
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
