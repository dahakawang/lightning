package net.davidvoid.thor.lightning.data.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.List;

import net.davidvoid.thor.lightning.config.RootConfig;
import net.davidvoid.thor.lightning.data.source.MongoDataSource;
import net.davidvoid.thor.lightning.entity.Group;
import net.davidvoid.thor.lightning.entity.User;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = { RootConfig.class })
public class GroupStoreTest {
    private String[] USER_DATA_FIXTURE = {
            "{'_id': 1, 'id': {'$numberLong': '1'}, 'name': 'david', 'password': '123'}",
            "{'_id': 2, 'id': {'$numberLong': '2'}, 'name': 'kevin', 'password': '456'}",
            "{'_id': 3, 'id': {'$numberLong': '3'}, 'name': 'paul', 'password': '789'}",
            "{'_id': 4, 'id': {'$numberLong': '4'}, 'name': 'marks', 'password': '321'}", };

    private String[] GROUP_DATA_FIXTURE = {
            "{'_id': 1, 'user_id': {'$numberLong': '1'}, 'id': {'$numberLong': '1'}, 'name': 'Group1'}",
            "{'_id': 2, 'user_id': {'$numberLong': '1'}, 'id': {'$numberLong': '2'}, 'name': 'Group2'}",
            "{'_id': 3, 'user_id': {'$numberLong': '1'}, 'id': {'$numberLong': '3'}, 'name': 'Group3'}",
            "{'_id': 4, 'user_id': {'$numberLong': '3'}, 'id': {'$numberLong': '4'}, 'name': 'Group4'}",
            "{'_id': 5, 'user_id': {'$numberLong': '3'}, 'id': {'$numberLong': '5'}, 'name': 'Group5'}", };

    @Autowired
    MongoDataSource source = null;

    @Autowired
    UserStore user_store = null;

    @Autowired
    GroupStore group_store = null;

    MongoCollection<Document> user_col = null;
    MongoCollection<Document> group_col = null;

    @Before
    public void setUp() throws Exception {
        user_col = source.getDatabase().getCollection("user");
        group_col = source.getDatabase().getCollection("group");

        for (String json : USER_DATA_FIXTURE) {
            Document object = Document.parse(json);
            user_col.insertOne(object);
        }

        for (String json : GROUP_DATA_FIXTURE) {
            Document object = Document.parse(json);
            group_col.insertOne(object);
        }
    }

    @After
    public void tearDown() throws Exception {
        user_col.drop();
        group_col.drop();
    }

    @Test
    public void GetGroups_GivenValidUser_WillReturnAllGroupsForTheUser() {
        User user = user_store.getByName("david");
        List<Group> groups = group_store.getGroups(user);

        assertEquals(3, groups.size());
        Collections.sort(groups, (Group left, Group right) -> ((Long) left
                .getId()).compareTo(right.getId()));
        
        Group g = groups.get(0);
        assertEquals(user, g.getUser());
        assertEquals(1, g.getId());
        assertEquals("Group1", g.getName());
        
        g = groups.get(1);
        assertEquals(user, g.getUser());
        assertEquals(2, g.getId());
        assertEquals("Group2", g.getName());

        g = groups.get(2);
        assertEquals(user, g.getUser());
        assertEquals(3, g.getId());
        assertEquals("Group3", g.getName());
        
        user = user_store.getByName("kevin");
        groups = group_store.getGroups(user);
        assertEquals(0, groups.size());
    }

    @Test
    public void GetGroups_GivenInvalidUser_WillThrow() {
        User user = new User();
        user.setName("david");
        
        try {
            group_store.getGroups(user);
            fail("Should throw");
        } catch (IllegalArgumentException e) {}
    }

    @Test
    public void Add_WithValidUser_WillInsert() {
        group_col.deleteMany(new Document());
        assertEquals(0, group_col.count());
        
        User user = user_store.getByName("david");
        Group group = new Group();
        group.setName("group1");
        group.setUser(user);
        
        group_store.add(group);
        
        assertEquals(1, group_col.count());
        Document obj = group_col.find().first();
        assertEquals(user.getId(), obj.get("user_id"));
        assertEquals("group1", obj.get("name"));
        assertTrue(group.has_valid_id());
    }

    @Test
    public void Add_WithInvalidUser_WillThrow() {
        group_col.deleteMany(new Document());
        assertEquals(0, group_col.count());
        
        User user = user_store.getByName("david");
        user.setInvalidId();
        
        Group group = new Group();
        group.setName("group1");
        try {
            group_store.add(group);
            fail("should throw");
        } catch (IllegalArgumentException e) {}

        group.setUser(user);
        try {
            group_store.add(group);
            fail("should throw");
        } catch (IllegalArgumentException e) {}
    }

    @Test
    public void Delete_aValidGroup_WillSuccess() {
        User user = user_store.getByName("david");
        List<Group> groups = group_store.getGroups(user);
        assertEquals(3, groups.size());

        group_store.delete(groups.get(0));

        groups = group_store.getGroups(user);
        assertEquals(2, groups.size());
    }

    @Test
    public void Delete_aValidGroupById_WillSuccess() {
        User user = user_store.getByName("david");
        List<Group> groups = group_store.getGroups(user);
        assertEquals(3, groups.size());

        Group group = new Group();
        group.setId(2);
        group.setUser(user);
        group_store.delete(group);

        groups = group_store.getGroups(user);
        assertEquals(2, groups.size());
    }

    @Test
    public void Delete_anInvalidGroup_WillThrow() {
        try {
            group_store.delete(null);
            fail("should throw");
        } catch (IllegalArgumentException e) {}

        try {
            Group group = new Group();
            group_store.delete(group);
            fail("should throw");
        } catch (IllegalArgumentException e) {}

        try {
            Group group = new Group();
            group.setId(1);
            group_store.delete(group);
            fail("should throw");
        } catch (IllegalArgumentException e) {}

        try {
            User user = new User();
            Group group = new Group();
            group.setUser(user);
            group.setId(1);
            group_store.delete(group);
            fail("should throw");
        } catch (IllegalArgumentException e) {}

        try {
            User user = new User();
            user.setId(1);
            Group group = new Group();
            group.setUser(user);
            group_store.delete(group);
            fail("should throw");
        } catch (IllegalArgumentException e) {}
    }

    @Test
    public void Update_validGroup_WillSuccess() {
        User user = user_store.getByName("david");
        List<Group> groups = group_store.getGroups(user);
        Group group = groups.get(0);
        
        group.setName("pipe");
        group_store.update(group);
        
        Document obj = group_col.find(new BasicDBObject("id", group.getId())).first();
        assertNotNull(obj);
        assertEquals("pipe", obj.get("name"));
    }

    @Test
    public void Update_anInvalidGroup_WillThrow() {
        try {
            group_store.update(null);
            fail("should throw");
        } catch (IllegalArgumentException e) {}

        try {
            Group group = new Group();
            group_store.update(group);
            fail("should throw");
        } catch (IllegalArgumentException e) {}

        try {
            Group group = new Group();
            group.setId(1);
            group_store.update(group);
            fail("should throw");
        } catch (IllegalArgumentException e) {}

        try {
            User user = new User();
            Group group = new Group();
            group.setUser(user);
            group.setId(1);
            group_store.update(group);
            fail("should throw");
        } catch (IllegalArgumentException e) {}

        try {
            User user = new User();
            user.setId(1);
            Group group = new Group();
            group.setUser(user);
            group_store.delete(group);
            fail("should throw");
        } catch (IllegalArgumentException e) {}
    }
}
