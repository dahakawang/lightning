package net.davidvoid.thor.lightning.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import net.davidvoid.thor.lightning.config.RootConfig;
import net.davidvoid.thor.lightning.data.access.GroupStore;
import net.davidvoid.thor.lightning.data.access.UserStore;
import net.davidvoid.thor.lightning.data.source.MongoDataSource;
import net.davidvoid.thor.lightning.entity.Group;
import net.davidvoid.thor.lightning.entity.User;
import net.davidvoid.thor.lightning.exception.ResourceNotFoundException;
import net.davidvoid.thor.lightning.service.security.ThorAuthentication;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by david on 5/5/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = { RootConfig.class })
public class GroupServiceTest {
    @Autowired
    UserStore users = null;
    @Autowired
    GroupStore store= null;
    @Autowired
    MongoDataSource source = null;
    @Autowired
    GroupService service = null;

    @Before
    public void setup() {
        source.getDatabase().drop();
        User user = new User();
        user.setName("david");
        user.setPassword("123");
        users.add(user);

        ThorAuthentication token = new ThorAuthentication("david", user.getId());
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    @Test
    public void add_validGroup_WillAdded() throws Exception {
        Group group = new Group();
        group.setName("news");

        service.add(group);
        assertNotNull(group.getUser());

        Group saved = store.getGroupById(group.getId());
        assertEquals("news", saved.getName());

        group = new Group();
        group.setName("technology");
        service.add(group);

        User user = users.getUser();
        List<Group> groups = store.getGroups(user);
        assertEquals(2, groups.size());

    }

    @Test
    public void getAllGroups_WhenHasGroup_ReturnAllGroup() {
        Group group = new Group();
        group.setName("news");
        service.add(group);

        group = new Group();
        group.setName("life");
        service.add(group);

        group = new Group();
        group.setName("technology");
        service.add(group);

        User user = users.getUser();
        List<Group> groups = store.getGroups(user);
        assertEquals(3, groups.size());
    }

    @Test
    public void getAllGroups_WhenNoGroup_ReturnEmpty() {
        User user = users.getUser();
        List<Group> groups = store.getGroups(user);
        assertEquals(0, groups.size());
    }


    @Test
    public void update_WhenValid_WillUpdate() {
        Group group = new Group();
        group.setName("news");
        service.add(group);

        group = new Group();
        group.setName("life");
        service.add(group);

        group = new Group();
        group.setName("technology");
        service.add(group);

        User user = users.getUser();
        List<Group> groups = store.getGroups(user);
        assertEquals(3, groups.size());

        group.setName("tick");
        service.update(group);

        Group saved = store.getGroupById(group.getId());
        assertEquals(3, groups.size());
        assertEquals("tick", saved.getName());
    }

    @Test
    public void update_WhenNoGroup_WillUpdate() {
        Group group = new Group();
        group.setName("news");
        group.setId(123);
        try {
            service.update(group);
            fail();
        } catch (ResourceNotFoundException e) {}
    }

    @Test
    public void update_WhenWrongId_WillUpdate() {
        Group group = new Group();
        group.setName("news");
        service.add(group);

        group.setId(123);
        try {
            service.update(group);
            fail();
        } catch (ResourceNotFoundException e) {}
    }

    @Test
    public void delete_GivenValidGroup_WillDeleteGroup() {
        Group group = new Group();
        group.setName("news");
        service.add(group);

        group = new Group();
        group.setName("life");
        service.add(group);

        group = new Group();
        group.setName("technology");
        service.add(group);

        User user = users.getUser();
        List<Group> groups = store.getGroups(user);
        assertEquals(3, groups.size());

        service.delete(group.getId());
        groups = store.getGroups(user);
        assertEquals(2, groups.size());
    }

    @Test
    public void delete_WhenNoGroup_WillThrow() {
        try {
            service.delete(123);
            fail();
        } catch (ResourceNotFoundException e) {}
    }
}