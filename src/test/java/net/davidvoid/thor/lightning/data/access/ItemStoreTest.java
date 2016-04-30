package net.davidvoid.thor.lightning.data.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.List;

import net.davidvoid.thor.lightning.config.RootConfig;
import net.davidvoid.thor.lightning.data.access.ItemStore.FILTER_OPTION;
import net.davidvoid.thor.lightning.data.access.ItemStore.SORT_OPTION;
import net.davidvoid.thor.lightning.data.source.MongoDataSource;
import net.davidvoid.thor.lightning.entity.Feed;
import net.davidvoid.thor.lightning.entity.Item;

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
public class ItemStoreTest {
    
    private String[] DATA_FIXTURE = {
            "{'_id': 1, 'feed_id': {'$numberLong': '1'}, 'id': {'$numberLong': '1'}, 'name': 'article2', 'author': 'author1', 'content': 'content1', 'url': 'url1', 'is_saved': true, 'is_read': false, 'last_update': {'$date': '2016-01-02T00:00:00.000Z'}}",
            "{'_id': 2, 'feed_id': {'$numberLong': '1'}, 'id': {'$numberLong': '2'}, 'name': 'article3', 'author': 'author2', 'content': 'content2', 'url': 'url2', 'is_saved': true, 'is_read': false, 'last_update': {'$date': '2016-01-07T00:00:00.000Z'}}",
            "{'_id': 3, 'feed_id': {'$numberLong': '1'}, 'id': {'$numberLong': '3'}, 'name': 'article0', 'author': 'author3', 'content': 'content3', 'url': 'url3', 'is_saved': true, 'is_read': true, 'last_update': {'$date': '2016-01-04T00:00:00.000Z'}}",
            "{'_id': 4, 'feed_id': {'$numberLong': '1'}, 'id': {'$numberLong': '4'}, 'name': 'article6', 'author': 'author4', 'content': 'content4', 'url': 'url4', 'is_saved': true, 'is_read': true, 'last_update': {'$date': '2016-01-09T00:00:00.000Z'}}",
            "{'_id': 5, 'feed_id': {'$numberLong': '1'}, 'id': {'$numberLong': '5'}, 'name': 'article5', 'author': 'author5', 'content': 'content5', 'url': 'url5', 'is_saved': true, 'is_read': false, 'last_update': {'$date': '2016-01-06T00:00:00.000Z'}}",
            "{'_id': 6, 'feed_id': {'$numberLong': '1'}, 'id': {'$numberLong': '6'}, 'name': 'article4', 'author': 'author6', 'content': 'content6', 'url': 'url6', 'is_saved': true, 'is_read': false, 'last_update': {'$date': '2016-01-01T00:00:00.000Z'}}",
    };

    @Autowired
    MongoDataSource source = null;

    @Autowired
    ItemStore store = null;

    MongoCollection<Document> collection = null;

    @Before
    public void setUp() throws Exception {
        collection = source.getDatabase().getCollection("item");

        for (String json : DATA_FIXTURE) {
            Document object = Document.parse(json);
            collection.insertOne(object);
        }
    }

    @After
    public void tearDown() throws Exception {
        collection.drop();
    }

    @Test
    public void GetItem_GivenValidFeed_WillReturnAllItemsOfTheFeed() {
        Feed feed = new Feed();
        feed.setId(1);
        
        List<Item> items = store.getItemsFromFeed(feed);
        assertEquals(6, items.size());

        feed.setId(2);
        
        items = store.getItemsFromFeed(feed);
        assertEquals(0, items.size());
    }

    @Test
    public void GetItem_GivenValidFeed_WillReturnItemBasedOnTheFilteringSortingStrategy() {
        Feed feed = new Feed();
        feed.setId(1);
        
        List<Item> items = store.getItemsFromFeed(feed, 0, 100, FILTER_OPTION.BOTH, SORT_OPTION.BY_NAME_ASC);
        assertEquals(6, items.size());
        Item item = items.get(0);
        assertEquals(feed, item.getFeed());
        assertEquals(3L, item.getId());
        assertEquals("article0", item.getName());
        assertEquals("author3", item.getAuthor());
        assertEquals("content3", item.getContent());
        assertEquals("url3", item.getUrl());
        assertEquals(true, item.isRead());
        assertEquals(true, item.isSaved());
        assertEquals(3L, items.get(0).getId());
        assertEquals(1L, items.get(1).getId());
        assertEquals(2L, items.get(2).getId());
        assertEquals(6L, items.get(3).getId());
        assertEquals(5L, items.get(4).getId());
        assertEquals(4L, items.get(5).getId());

        items = store.getItemsFromFeed(feed, 0, 100, FILTER_OPTION.BOTH, SORT_OPTION.BY_NAME_DSC);
        assertEquals(6, items.size());
        assertEquals(3L, items.get(5).getId());
        assertEquals(1L, items.get(4).getId());
        assertEquals(2L, items.get(3).getId());
        assertEquals(6L, items.get(2).getId());
        assertEquals(5L, items.get(1).getId());
        assertEquals(4L, items.get(0).getId());

        items = store.getItemsFromFeed(feed, 0, 100, FILTER_OPTION.BOTH, SORT_OPTION.BY_UPDATE_DATE_ASC);
        assertEquals(6, items.size());
        assertEquals(6L, items.get(0).getId());
        assertEquals(1L, items.get(1).getId());
        assertEquals(3L, items.get(2).getId());
        assertEquals(5L, items.get(3).getId());
        assertEquals(2L, items.get(4).getId());
        assertEquals(4L, items.get(5).getId());

        items = store.getItemsFromFeed(feed, 0, 100, FILTER_OPTION.BOTH, SORT_OPTION.BY_UPDATE_DATE_DSC);
        assertEquals(6, items.size());
        assertEquals(6L, items.get(5).getId());
        assertEquals(1L, items.get(4).getId());
        assertEquals(3L, items.get(3).getId());
        assertEquals(5L, items.get(2).getId());
        assertEquals(2L, items.get(1).getId());
        assertEquals(4L, items.get(0).getId());

        items = store.getItemsFromFeed(feed, 0, 100, FILTER_OPTION.ONLY_READ, SORT_OPTION.BY_NAME_ASC);
        assertEquals(2, items.size());
        items = store.getItemsFromFeed(feed, 0, 100, FILTER_OPTION.ONLY_UNREAD, SORT_OPTION.BY_NAME_ASC);
        assertEquals(4, items.size());

        items = store.getItemsFromFeed(feed, 0, 2, FILTER_OPTION.BOTH, SORT_OPTION.BY_UPDATE_DATE_DSC);
        assertEquals(2, items.size());
        assertEquals(4L, items.get(0).getId());
        assertEquals(2L, items.get(1).getId());

        items = store.getItemsFromFeed(feed, 4, 3, FILTER_OPTION.BOTH, SORT_OPTION.BY_UPDATE_DATE_DSC);
        assertEquals(2, items.size());
        assertEquals(1L, items.get(0).getId());
        assertEquals(6L, items.get(1).getId());
    }
    
    @Test
    public void GetItem_GivenInvalidFeed_WillThrow() {
        Feed feed = new Feed();
        
        try {
            store.getItemsFromFeed(null);
            fail("should throw");
        } catch (IllegalArgumentException e) {}

        try {
            store.getItemsFromFeed(feed);
            fail("should throw");
        } catch (IllegalArgumentException e) {}

        try {
            store.getItemsFromFeed(null, 0, 100, FILTER_OPTION.BOTH, SORT_OPTION.BY_UPDATE_DATE_DSC);
            fail("should throw");
        } catch (IllegalArgumentException e) {}

        try {
            store.getItemsFromFeed(feed, 0, 100, FILTER_OPTION.BOTH, SORT_OPTION.BY_UPDATE_DATE_DSC);
            fail("should throw");
        } catch (IllegalArgumentException e) {}
    }
    
    @Test 
    public void AddItem_GivenValidFeed_WillInsert() {
        Feed feed = new Feed();
        feed.setId(1);
        Date date = new Date();
        
        Item item = new Item();
        item.setFeed(feed);
        item.setAuthor("david");
        item.setContent("hello world");
        item.setIsRead(false);
        item.setIsSaved(false);
        item.setLastUpdate(date);
        item.setName("on constructing a robust distribute system");
        item.setUrl("www.fake.com");
        
        store.add(item);
        assertEquals(7, collection.count());
        
        
        Document object = collection.find(new BasicDBObject("last_update", date)).first();
        assertNotNull(object);
        assertEquals("on constructing a robust distribute system", object.get("name"));
        assertEquals(1L, object.get("feed_id"));
        assertEquals("hello world", object.get("content"));
        assertEquals("david", object.get("author"));
        assertEquals("www.fake.com", object.get("url"));
        assertEquals(false, object.get("is_read"));
        assertEquals(false, object.get("is_saved"));
    }

    @Test 
    public void AddItem_GivenInvalidItem_WillThrow() {
        try {
            Feed feed = new Feed();
            feed.setId(1);
        
            Item item = new Item();
            item.setId(2);
            item.setFeed(feed);
            store.add(item);
            fail("should throw");
        } catch (IllegalArgumentException e) {}

        try {
            Feed feed = new Feed();

            Item item = new Item();
            item.setFeed(feed);
            store.add(item);
            fail("should throw");
        } catch (IllegalArgumentException e) {}

        try {
            Item item = new Item();
            store.add(item);
            fail("should throw");
        } catch (IllegalArgumentException e) {}

        try {
            store.add(null);
            fail("should throw");
        } catch (IllegalArgumentException e) {}
    }
    
    @Test 
    public void UpdateItem_GivenValidItem_WillUpdate() {
        Feed feed = new Feed();
        feed.setId(1);
        Item item = store.getItemsFromFeed(feed).get(0);
        
        item.setAuthor("1234");
        store.update(item);
        
        assertEquals(6, collection.count());
        Document object = collection.find(new BasicDBObject("id", item.getId())).first();
        assertNotNull(object);
        assertEquals("1234", object.get("author"));
    }

    @Test 
    public void UpdateItem_GivenInvalidItem_WillThrow() {
        try {
            store.update(null);
            fail("should throw");
        } catch (IllegalArgumentException e) {}

        try {
            Item item = new Item();
            store.update(item);
            fail("should throw");
        } catch (IllegalArgumentException e) {}
    }

    @Test 
    public void DeleteItem_GivenValidItem_WillUpdate() {
        Feed feed = new Feed();
        feed.setId(1);
        Item item = store.getItemsFromFeed(feed).get(0);
        assertEquals(6, collection.count());

        store.delete(item);
        
        assertEquals(5, collection.count());
    }

    @Test 
    public void DeleteItem_GivenValidId_WillDelete() {
        assertEquals(6, collection.count());

        Long id = 2L;
        store.deleteById(id);
        
        assertEquals(5, collection.count());
    }

    @Test 
    public void DeleteItem_GivenInvalidItem_WillThrow() {
        try {
            store.delete(null);
            fail("should throw");
        } catch (IllegalArgumentException e) {}

        try {
            Item item = new Item();
            store.delete(item);
            fail("should throw");
        } catch (IllegalArgumentException e) {}
    }
}
