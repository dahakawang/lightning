package net.davidvoid.thor.lightning.data.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.util.Assert.isInstanceOf;
import net.davidvoid.thor.lightning.config.RootConfig;
import net.davidvoid.thor.lightning.data.source.MongoDataSource;
import net.davidvoid.thor.lightning.entity.User;
import net.davidvoid.thor.lightning.exception.ResourceNotFoundException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

/**
 * Created by david on 3/22/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = { RootConfig.class })
public class UserStoreTest {
    private String[] DATA_FIXTURE = {
            "{'_id': 1, 'id': {'$numberLong': '1'}, 'name': 'david', 'password': '123'}",
            "{'_id': 2, 'id': {'$numberLong': '2'}, 'name': 'kevin', 'password': '456'}",
            "{'_id': 3, 'id': {'$numberLong': '3'}, 'name': 'paul', 'password': '789'}",
            "{'_id': 4, 'id': {'$numberLong': '4'}, 'name': 'marks', 'password': '321'}", };

    @Autowired
    MongoDataSource source = null;

    @Autowired
    UserStore store = null;

    @Before
    public void setUp() throws Exception {
        DBCollection collection = source.getDatabase().getCollection("user");

        for (String json : DATA_FIXTURE) {
            DBObject object = (DBObject) JSON.parse(json);
            collection.insert(object);
        }
    }

    @After
    public void tearDown() throws Exception {
        source.getDatabase().getCollection("user").drop();
    }

    @Test
    public void GetByName_WhenGivenValidUserName_WillReturnUser()
            throws Exception {
        User user = store.getByName("david");
        assertEquals("david", user.getName());
        assertEquals("123", user.getPassword());
        assertEquals(1, user.getId());
    }

    @Test
    public void testGetById() throws Exception {
        User user = store.getById(1);
        assertEquals("david", user.getName());
        assertEquals("123", user.getPassword());
        assertEquals(1, user.getId());
    }

    @Test
    public void GetUser_GivenBadNameOrId_WillThrowNotFound() {
        try {
            store.getByName("123");
            fail("need throw");
        } catch (ResourceNotFoundException e) {
        }

        try {
            store.getByName("");
            fail("need throw");
        } catch (IllegalArgumentException e) {
        }

        try {
            store.getById(-23);
            fail("need throw");
        } catch (ResourceNotFoundException e) {
        }

        try {
            store.getById(12345);
            fail("need throw");
        } catch (ResourceNotFoundException e) {
        }
    }

    @Test
    public void Add_NewUsers_WillSuccess() throws Exception {
        DBCollection collection = source.getDatabase().getCollection("user");
        collection.remove(null);

        assertEquals(0, collection.count());

        User user1 = new User();
        user1.setName("david");
        user1.setPassword("123");
        store.add(user1);

        assertEquals(1, collection.count());

        User user2 = new User();
        user2.setName("kevin");
        user2.setPassword("456");
        store.add(user2);

        assertEquals(2, collection.count());

        DBObject object1 = collection
                .findOne(new BasicDBObject("name", "david"));
        assertNotNull(object1);
        assertEquals("david", object1.get("name"));
        assertEquals("123", object1.get("password"));
        isInstanceOf(Long.class, object1.get("id"));
        assertEquals(4, object1.keySet().size());

        DBObject object2 = collection
                .findOne(new BasicDBObject("name", "kevin"));
        assertNotNull(object2);
        assertEquals("kevin", object2.get("name"));
        assertEquals("456", object2.get("password"));
        isInstanceOf(Long.class, object2.get("id"));
        assertEquals(4, object2.keySet().size());

        long id1 = (long) object1.get("id");
        long id2 = (long) object2.get("id");
        assertTrue(id1 != id2);
    }

    @Test
    public void Add_SavedUsers_WillNotInsertAndThrow() {
        DBCollection collection = source.getDatabase().getCollection("user");
        collection.remove(null);

        User user1 = new User();
        user1.setName("david");
        user1.setPassword("123");
        store.add(user1);

        try {
            store.add(user1);
            fail("should throw an exception");
        } catch (IllegalArgumentException e) {
        }

        assertEquals(1, collection.count());
    }

    @Test
    public void Delete_ExistingUser_WillSuccess() throws Exception {
        DBCollection collection = source.getDatabase().getCollection("user");

        assertEquals(4, collection.count());
        User user1 = store.getByName("david");
        store.delete(user1);

        assertEquals(3, collection.count());

        DBObject object = collection
                .findOne(new BasicDBObject("name", "david"));
        assertNull(object);

    }

    @Test
    public void Delete_byId_WillSuccess() {
        DBCollection collection = source.getDatabase().getCollection("user");
        assertEquals(4, collection.count());

        User user = new User();
        user.setId(2);
        store.delete(user);
        assertEquals(3, collection.count());

        DBObject object = collection
                .findOne(new BasicDBObject("name", "kevin"));
        assertNull(object);
    }

    @Test
    public void Delete_byIdSeveralTimes_FirstWillSuccessRemainWillHaveNoEffects() {
        DBCollection collection = source.getDatabase().getCollection("user");
        assertEquals(4, collection.count());

        User user = new User();
        user.setId(2);
        store.delete(user);
        assertEquals(3, collection.count());

        user.setId(2);
        store.delete(user);
        assertEquals(3, collection.count());

        user.setId(2);
        store.delete(user);
        assertEquals(3, collection.count());

        DBObject object = collection
                .findOne(new BasicDBObject("name", "kevin"));
        assertNull(object);
    }

    @Test
    public void Delete_InvalidUser_WillThrow() {
        DBCollection collection = source.getDatabase().getCollection("user");

        assertEquals(4, collection.count());

        User user1 = new User();
        user1.setName("david");
        user1.setPassword("123");
        try {
            store.delete(user1);
            fail("should throw an exception");
        } catch (IllegalArgumentException e) {
        }

        assertEquals(4, collection.count());
    }

    @Test
    public void Delete_UserSeveralTimes_WillThrow() {
        DBCollection collection = source.getDatabase().getCollection("user");
        assertEquals(4, collection.count());

        User user = store.getByName("david");
        store.delete(user);
        assertEquals(3, collection.count());

        try {
            store.delete(user);
            fail("should throw an exception");
        } catch (IllegalArgumentException e) {
        }
        assertEquals(3, collection.count());
    }

    @Test
    public void Update_User_WillSuccess() throws Exception {
        User user = store.getById(1);
        assertEquals("david", user.getName());
        assertEquals("123", user.getPassword());
        assertEquals(1, user.getId());

        user.setName("david1");
        user.setPassword("7788");
        store.update(user);

        user = store.getById(1);
        assertEquals("david1", user.getName());
        assertEquals("7788", user.getPassword());
        assertEquals(1, user.getId());

    }

    @Test
    public void Update_InvalidUser_WillThrow() {
        User user1 = new User();
        user1.setName("david");
        user1.setPassword("123");
        try {
            store.update(user1);
            fail("should throw an exception");
        } catch (IllegalArgumentException e) {
        }

    }
}