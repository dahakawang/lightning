package net.davidvoid.thor.lightning.data.access;

import com.mongodb.DBCollection;
import net.davidvoid.thor.lightning.config.RootConfig;
import net.davidvoid.thor.lightning.data.source.MongoDataSource;
import net.davidvoid.thor.lightning.entity.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * Created by david on 3/22/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {RootConfig.class})
public class UserStoreTest {

    @Autowired
    MongoDataSource source = null;

    @Autowired
    UserStore store = null;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetByName() throws Exception {
    }

    @Test
    public void testGetById() throws Exception {

    }

    @Test
    public void testAdd() throws Exception {
        User user = new User();
        user.setName("david");
        user.setPassword("123");
        store.add(user);

        user = new User();
        user.setName("kevin");
        user.setPassword("456");
        store.add(user);

        DBCollection collection = source.getDatabase().getCollection("user");
        assertEquals(2, collection.count());
    }

    @Test
    public void testDelete() throws Exception {

    }

    @Test
    public void testUpdate() throws Exception {

    }
}