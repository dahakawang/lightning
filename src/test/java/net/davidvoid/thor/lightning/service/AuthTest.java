package net.davidvoid.thor.lightning.service;

import net.davidvoid.thor.lightning.config.RootConfig;
import net.davidvoid.thor.lightning.data.access.UserStore;
import net.davidvoid.thor.lightning.data.source.MongoDataSource;
import net.davidvoid.thor.lightning.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

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
        user.setPassword("123");
        store.add(user);
    }


    @Test
    public void authenticate_WhenHasUserAndPasswordRight_WillSuccess() throws Exception {
        User user = auth.authenticate("david", "123");
        assertNotNull(user);
        assertEquals("david", user.getName());
        assertEquals("123", shaDigest(user.getPassword()));
    }

    @Test
    public void testUpdate() throws Exception {

    }

    @Test
    public void testRegister() throws Exception {

    }

    @Test
    public void testGetUser() throws Exception {

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