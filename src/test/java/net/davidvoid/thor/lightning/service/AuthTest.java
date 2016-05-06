package net.davidvoid.thor.lightning.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.davidvoid.thor.lightning.config.RootConfig;
import net.davidvoid.thor.lightning.data.access.UserStore;
import net.davidvoid.thor.lightning.data.source.MongoDataSource;
import net.davidvoid.thor.lightning.entity.User;
import net.davidvoid.thor.lightning.exception.AuthenticationException;
import net.davidvoid.thor.lightning.exception.DuplicateUserException;
import net.davidvoid.thor.lightning.exception.ResourceNotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by david on 5/5/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = { RootConfig.class })
public class AuthTest {

    @Autowired
    UserStore store = null;
    @Autowired
    MongoDataSource source = null;
    @Autowired
    Auth auth = null;

    @Before
    public void setup() {
        source.getDatabase().drop();

        User user = new User();
        user.setName("david");
        user.setPassword(shaDigest("123"));
        store.add(user);
    }

    @Test
    public void authenticate_WhenHasUserAndPasswordRight_WillSuccess() throws Exception {
        User user = auth.authenticate("david", "123");
        assertNotNull(user);
        assertEquals("david", user.getName());
        assertEquals(shaDigest("123"), user.getPassword());
    }

    @Test
    public void authenticate_WhenNoUser_WillThrow() throws Exception {
        source.getDatabase().drop();

        try {
            auth.authenticate("david", "123");
            fail();
        } catch (AuthenticationException e) {}
    }

    @Test
    public void authenticate_WhenWrongPassword_WillThrow() throws Exception {
        try {
            auth.authenticate("david", "1234");
            fail();
        } catch (AuthenticationException e) {}
    }

    @Test
    public void update_givenValidUser_willUpdate() throws Exception {
        auth.update("david", "aabbab");
        User user = store.getUser();
        assertEquals("david", user.getName());
        assertEquals(shaDigest("aabbab"), user.getPassword());
    }

    @Test
    public void update_WhenNoUser_willThrow() throws Exception {
        source.getDatabase().drop();;

        try {
            auth.update("david", "aabbab");
            fail();
        } catch (ResourceNotFoundException e) {}
    }

    @Test
    public void update_WhenTryToUpdateName_willThrow() throws Exception {
        try {
            auth.update("kevin", "aabbab");
            fail();
        } catch (ResourceNotFoundException e) {}
    }

    @Test
    public void register_givenValidUser_willAdd() throws Exception {
        source.getDatabase().drop();

        auth.register("david", "123");
        User user = store.getUser();
        assertEquals("david", user.getName());
        assertEquals(shaDigest("123"), user.getPassword());
    }

    @Test
    public void register_WhenAlreadyHasUser_WillThrow() throws Exception {
        try {
            auth.register("david", "123");
            fail();
        } catch (DuplicateUserException e) {}
    }

    @Test
    public void getUser_WhenHasUser_WillReturnUser() throws Exception {
        User user = auth.getUser("david");
        assertEquals("david", user.getName());
        assertEquals(shaDigest("123"), user.getPassword());
    }

    @Test
    public void getUser_WhenUseNonexistUser_WillReturnUser() throws Exception {
        try {
            auth.getUser("ken");
            fail();
        } catch (ResourceNotFoundException e) {}
    }

    @Test
    public void getUser_WhenUserNotExistAtAll_WillReturnUser() throws Exception {
        source.getDatabase().drop();

        try {
            auth.getUser("ken");
            fail();
        } catch (ResourceNotFoundException e) {}
    }

    private String shaDigest(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());

            return String.format("%064x",  new BigInteger(1, md.digest()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not supported");
        }
    }
}