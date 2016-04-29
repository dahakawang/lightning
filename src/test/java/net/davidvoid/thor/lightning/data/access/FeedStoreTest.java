package net.davidvoid.thor.lightning.data.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.davidvoid.thor.lightning.config.RootConfig;
import net.davidvoid.thor.lightning.data.source.MongoDataSource;
import net.davidvoid.thor.lightning.entity.Feed;
import net.davidvoid.thor.lightning.entity.Group;

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

/**
 * Created by david on 3/23/16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = { RootConfig.class })
public class FeedStoreTest {

    private String[] FEED_DATA_FIXTURE = {
            "{'_id': 1, 'id': {'$numberLong': '1'}, 'group_id': {'$numberLong': '1'}, 'name': 'feed1', 'description': 'a small feed1', 'url': 'www.feed1.com', 'last_update': {'$date': '2016-01-02T00:00:00.000Z'}}",
            "{'_id': 2, 'id': {'$numberLong': '2'}, 'group_id': {'$numberLong': '1'}, 'name': 'feed2', 'description': 'a small feed2', 'url': 'www.feed2.com', 'last_update': {'$date': '2016-01-02T00:00:00.000Z'}}",
            "{'_id': 3, 'id': {'$numberLong': '3'}, 'group_id': {'$numberLong': '2'}, 'name': 'feed3', 'description': 'a small feed3', 'url': 'www.feed3.com', 'last_update': {'$date': '2016-01-02T00:00:00.000Z'}}",
    };

    @Autowired
    MongoDataSource source = null;

    @Autowired
    FeedStore store = null;

    MongoCollection<Document> collection = null;

    @Before
    public void setUp() throws Exception {
        collection = source.getDatabase().getCollection("feed");

        for (String json : FEED_DATA_FIXTURE) {
            collection.insertOne(Document.parse(json));
        }
    }

    @After
    public void tearDown() throws Exception {
        collection.drop();
    }

    @Test
    public void GetFeeds_GivenValidRelation_WillReturnFeeds() throws Exception {
        Group group = new Group();
        group.setId(1L);

        List<Feed> feeds = store.getFeeds(group);
        assertEquals(2, feeds.size());
        Collections.sort(feeds, (Feed left, Feed right)->((Long)left.getId()).compareTo((Long)right.getId()));
        assertEquals(group, feeds.get(0).getGroup());
        assertEquals(group, feeds.get(1).getGroup());

        group.setId(2L);
        feeds = store.getFeeds(group);
        assertEquals(1, feeds.size());

        Feed feed = feeds.get(0);

        assertEquals(3L, feed.getId());
        assertEquals(group, feed.getGroup());
        assertEquals("feed3", feed.getName());
        assertEquals("a small feed3", feed.getDescription());
        assertEquals("www.feed3.com", feed.getUrl());
        assertNotNull(feed.getLastUpdate());
    }

    @Test
    public void Add_Feeds_WillSuccess() throws Exception {
        collection.deleteMany(new Document());

        Date date = new Date();
        Feed feed = new Feed();
        feed.setName("hehe");
        feed.setDescription("desc");
        feed.setUrl("www.baidu.com");
        feed.setLastUpdate(date);

        store.add(feed);

        assertEquals(1, collection.count());
        Document object = collection.find().first();
        assertEquals(6, object.keySet().size());
        assertEquals("hehe", object.get("name"));
        assertEquals("desc", object.get("description"));
        assertEquals("www.baidu.com", object.get("url"));
        assertEquals(date, object.get("last_update"));
        assertNotNull(object.get("id"));
    }

    @Test
    public void Delete_ValidFeed_WillSuccess() throws Exception {
        assertEquals(3, collection.count());
        Feed feed = new Feed();
        feed.setId(1);
        store.delete(feed);

        assertEquals(2, collection.count());
    }

    @Test
    public void Update_ValidFeed_WillSuccess() throws Exception {
        Date date = new Date();
        Feed feed = new Feed();
        feed.setId(1);
        feed.setLastUpdate(date);

        store.update(feed);

        assertEquals(3, collection.count());

        Document object = collection.find(new BasicDBObject("id", 1L)).first();
        assertEquals("", object.get("description"));
        assertEquals("", object.get("url"));
        assertEquals(date, object.get("last_update"));
        assertEquals(1L, object.get("id"));
    }
}