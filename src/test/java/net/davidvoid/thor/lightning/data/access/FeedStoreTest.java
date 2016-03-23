package net.davidvoid.thor.lightning.data.access;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import net.davidvoid.thor.lightning.config.RootConfig;
import net.davidvoid.thor.lightning.data.source.MongoDataSource;
import net.davidvoid.thor.lightning.entity.Feed;
import net.davidvoid.thor.lightning.entity.FeedRelation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by david on 3/23/16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = { RootConfig.class })
public class FeedStoreTest {

    private String[] FEED_DATA_FIXTURE = {
            "{'_id': 1, 'id': {'$numberLong': '1'}, 'description': 'a small feed1', 'url': 'www.feed1.com', 'lastUpdate': {'$date': '2016-01-02T00:00:00.000+8'}}",
            "{'_id': 2, 'id': {'$numberLong': '2'}, 'description': 'a small feed2', 'url': 'www.feed2.com', 'lastUpdate': {'$date': '2016-01-02T00:00:00.000+8'}}",
            "{'_id': 3, 'id': {'$numberLong': '3'}, 'description': 'a small feed3', 'url': 'www.feed3.com', 'lastUpdate': {'$date': '2016-01-02T00:00:00.000+8'}}",
    };

    @Autowired
    MongoDataSource source = null;

    @Autowired
    FeedStore store = null;

    DBCollection collection = null;

    @Before
    public void setUp() throws Exception {
        collection = source.getDatabase().getCollection("feed");

        for (String json : FEED_DATA_FIXTURE) {
            collection.insert((DBObject) JSON.parse(json));
        }
    }

    @After
    public void tearDown() throws Exception {
        collection.drop();
    }

    @Test
    public void GetFeeds_GivenValidRelation_WillReturnFeeds() throws Exception {
        List<FeedRelation> relations = new ArrayList<FeedRelation>();

        FeedRelation relation;
        relation = new FeedRelation();
        relation.setId(1);
        relation.setFeedId(1);
        relations.add(relation);

        relation = new FeedRelation();
        relation.setId(2);
        relation.setFeedId(2);
        relations.add(relation);

        relation = new FeedRelation();
        relation.setId(3);
        relation.setFeedId(3);
        relations.add(relation);

        List<Feed> feeds = store.getFeeds(relations);
        assertEquals(3, feeds.size());

        relations.clear();
        relations.add(relation);
        feeds = store.getFeeds(relations);
        assertEquals(1, feeds.size());

        Feed feed = feeds.get(0);

        assertEquals(3L, feed.getId());
        assertEquals("a small feed3", feed.getDescription());
        assertEquals("www.feed3.com", feed.getUrl());
        assertEquals(null, feed.getName());
        assertNotNull(feed.getLastUpdate());
    }

    @Test
    public void Add_Feeds_WillSuccess() throws Exception {
        collection.remove(null);

        Date date = new Date();
        Feed feed = new Feed();
        feed.setDescription("desc");
        feed.setUrl("www.baidu.com");
        feed.setLastUpdate(date);

        store.add(feed);

        assertEquals(1, collection.count());
        DBObject object = collection.findOne();
        assertEquals(5, object.keySet().size());
        assertEquals("desc", object.get("description"));
        assertEquals("www.baidu.com", object.get("url"));
        assertEquals(date, object.get("last_update"));
        assertNotNull(object.get("id"));
    }

    @Test
    public void testDelete() throws Exception {
        assertEquals(3, collection.count());
        Feed feed = new Feed();
        feed.setId(1);
        store.delete(feed);

        assertEquals(2, collection.count());
    }

    @Test
    public void testUpdate() throws Exception {
        Date date = new Date();
        Feed feed = new Feed();
        feed.setId(1);
        feed.setLastUpdate(date);

        assertEquals(3, collection.count());

        DBObject object = collection.findOne(new BasicDBObject("id", 3L));
        assertEquals("", object.get("description"));
        assertEquals("", object.get("url"));
        assertEquals(date, object.get("last_update"));
        assertEquals(3, object.get("id"));
    }
}