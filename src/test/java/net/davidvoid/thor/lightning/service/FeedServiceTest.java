package net.davidvoid.thor.lightning.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.List;

import net.davidvoid.thor.lightning.config.RootConfig;
import net.davidvoid.thor.lightning.data.access.FeedStore;
import net.davidvoid.thor.lightning.data.access.GroupStore;
import net.davidvoid.thor.lightning.data.access.UserStore;
import net.davidvoid.thor.lightning.data.source.MongoDataSource;
import net.davidvoid.thor.lightning.entity.Feed;
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
public class FeedServiceTest {
    @Autowired
    UserStore users = null;
    @Autowired
    GroupStore groups = null;
    @Autowired
    FeedStore store = null;
    @Autowired
    FeedService service = null;
    @Autowired
    MongoDataSource source = null;

    @Before
    public void setup() {
        source.getDatabase().drop();
        User user = new User();
        user.setName("david");
        user.setPassword("123");
        users.add(user);

        ThorAuthentication token = new ThorAuthentication("david", user.getId());
        SecurityContextHolder.getContext().setAuthentication(token);

        Group g1 = new Group();
        g1.setName("news");
        g1.setUser(user);
        Group g2 = new Group();
        g2.setUser(user);
        g2.setName("tech");
        Group g3 = new Group();
        g3.setUser(user);
        g3.setName("empty");
        groups.add(g1);
        groups.add(g2);
        groups.add(g3);

        Feed f1 = new Feed();
        f1.setDescription("feed1");
        f1.setName("feed1");
        f1.setUrl("www.feed1.com");
        service.add(g1.getId(), f1);

        Feed f2 = new Feed();
        f2.setDescription("feed2");
        f2.setName("feed2");
        f2.setUrl("www.feed2.com");
        service.add(g1.getId(), f2);

        Feed f3 = new Feed();
        f3.setDescription("feed3");
        f3.setName("feed3");
        f3.setUrl("www.feed3.com");
        service.add(g2.getId(), f3);
    }

    @Test
    public void add_GivenValidFeed_WillAdded() {
        long count = source.getDatabase().getCollection("feed").count();
        assertEquals(3, count);

        Feed feed = service.getFeeds(2).get(0);
        assertNotNull(feed.getLastUpdate());
    }

    @Test
    public void get_WhenHasFeed_ReturnAll() {
        Group group = groups.getGroupById(2);
        List<Feed> feeds = service.getFeeds(group.getId());
        assertEquals(1, feeds.size());
    }

    @Test
    public void get_WhenParentGroupNonExist_WillThrow() {
        try {
            service.getFeeds(128);
            fail();
        } catch (ResourceNotFoundException e) {}
    }

    @Test
    public void get_WhenGroupExistButEmpty_WillReturnEmpty() {
        List<Feed> feeds = service.getFeeds(3);
        assertEquals(0, feeds.size());
    }

    @Test
    public void update_WhenGivenValidFeed_WillUpdate() {
        Feed feed = service.getFeeds(2).get(0);
        feed.setDescription("feedm");
        feed.setUrl("feedm");
        feed.setName("feedm");
        feed.setGroup(groups.getGroupById(3));
        Date d = new Date();
        feed.setLastUpdate(d);
        service.update(2, feed);

        feed = service.getFeeds(2).get(0);
        assertEquals("feedm", feed.getDescription());
        assertEquals("www.feed3.com", feed.getUrl());
        assertEquals("feedm", feed.getName());
        assertNotEquals(d, feed.getLastUpdate());
    }

    @Test
    public void update_WhenTryingToChangeItsGroup_WillThrow() {
        Feed feed = service.getFeeds(2).get(0);
        try {
            service.update(1, feed); // when trying to move the belonging group of the feed
        }  catch (ResourceNotFoundException e) {}
    }

    @Test
    public void delete_WhenGivenValidFeed_WillDelete() {
        Feed feed = service.getFeeds(1).get(0);
        service.delete(1, feed.getId());
    }

    @Test
    public void delete_WhenGivenWrongFeedIdOrWrongGroupId_WillThrow() {
        Feed feed = service.getFeeds(1).get(0);
        try {
            service.delete(2, feed.getId());
            fail();
        } catch (ResourceNotFoundException e) {}

        try {
            feed.setId(123);
            service.delete(1, feed.getId());
            fail();
        } catch (ResourceNotFoundException e) {}
    }
}