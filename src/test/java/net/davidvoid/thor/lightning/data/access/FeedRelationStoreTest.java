package net.davidvoid.thor.lightning.data.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import com.mongodb.BasicDBObject;
import net.davidvoid.thor.lightning.config.RootConfig;
import net.davidvoid.thor.lightning.data.source.MongoDataSource;
import net.davidvoid.thor.lightning.entity.FeedRelation;
import net.davidvoid.thor.lightning.entity.Group;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = { RootConfig.class })
public class FeedRelationStoreTest {

    private String[] DATA_FIXTURE = {
            "{'_id': 1, 'group_id': {'$numberLong': '1'}, 'feed_id': {'$numberLong': '1'}, 'id': {'$numberLong': '1'}, 'name': 'feed1'}",
            "{'_id': 2, 'group_id': {'$numberLong': '1'}, 'feed_id': {'$numberLong': '2'}, 'id': {'$numberLong': '2'}, 'name': 'feed2'}",
            "{'_id': 3, 'group_id': {'$numberLong': '1'}, 'feed_id': {'$numberLong': '3'}, 'id': {'$numberLong': '3'}, 'name': 'feed3'}",
            "{'_id': 4, 'group_id': {'$numberLong': '2'}, 'feed_id': {'$numberLong': '2'}, 'id': {'$numberLong': '4'}, 'name': 'feed2'}",
            "{'_id': 5, 'group_id': {'$numberLong': '2'}, 'feed_id': {'$numberLong': '3'}, 'id': {'$numberLong': '5'}, 'name': 'feed3'}", };

    @Autowired
    MongoDataSource source = null;

    @Autowired
    FeedRelationStore store = null;

    DBCollection collection = null;

    @Before
    public void setUp() throws Exception {
        collection = source.getDatabase().getCollection("feed_relation");

        for (int i = 0; i < DATA_FIXTURE.length; ++i) {
            DBObject object = (DBObject) JSON.parse(DATA_FIXTURE[i]);
            collection.insert(object);
        }
    }

    @After
    public void tearDown() throws Exception {
        collection.drop();
    }

    @Test
    public void GetFeedRelations_GivenValidGroup_WillReturnAllRelation() {
        Group group = new Group();
        group.setId(2);
        
        List<FeedRelation> relations = store.getFeedRelations(group);
        assertEquals(2, relations.size());
        
        Collections.sort(relations, (FeedRelation left, FeedRelation right)->((Long)left.getId()).compareTo(right.getId()));
        
        FeedRelation r = relations.get(0);
        assertEquals(2, r.getGroupId());
        assertEquals(2, r.getFeedId());
        assertEquals(4, r.getId());
        assertEquals("feed2", r.getName());
        
        r = relations.get(1);
        assertEquals(2, r.getGroupId());
        assertEquals(3, r.getFeedId());
        assertEquals(5, r.getId());
        assertEquals("feed3", r.getName());
        
    }

    @Test
    public void GetFeedRelations_GivenInvalidGroup_WillThrow() {
        
        try {
            store.getFeedRelations(null);
            fail("should throw");
        } catch (IllegalArgumentException e) {}
        
        try {
            Group group = new Group();
            store.getFeedRelations(group);
            fail("should throw");
        } catch (IllegalArgumentException e) {}
    }

    @Test
    public void Add_ValidRelation_WillCreatenewInsertion() {
        collection.remove(null);

        FeedRelation relation = new FeedRelation();
        relation.setGroupId(1);
        relation.setFeedId(2);
        relation.setName("hello");
        store.add(relation);
        
        assertEquals(1, collection.count());
        DBObject obj = collection.findOne();
        assertNotNull(obj);
        
        assertEquals(5L, obj.keySet().size());
        assertEquals(1L, obj.get("group_id"));
        assertEquals(2L, obj.get("feed_id"));
        assertEquals("hello", obj.get("name"));
    }

    @Test
    public void Add_InvalidRelation_WillThrow() {
        FeedRelation relation = new FeedRelation();
        relation.setGroupId(1);
        relation.setFeedId(2);
        relation.setName("hello");
        relation.setId(12);
        
        try {
            store.add(null);
            fail("should throw");
        } catch (IllegalArgumentException e) {}

        try {
            store.add(relation);
            fail("should throw");
        } catch (IllegalArgumentException e) {}
    }

    @Test
    public void Delete_ValidRelation_WillSuccessful() {
        Group group = new Group();
        group.setId(2);
        List<FeedRelation> relations = store.getFeedRelations(group);
        assertEquals(2, relations.size());
        FeedRelation relation = relations.get(0);
        
        store.delete(relation);

        relations = store.getFeedRelations(group);
        assertEquals(1, relations.size());
    }

    @Test
    public void Delete_ValidRelationById_WillSuccessful() {
        Group group = new Group();
        group.setId(2);

        assertEquals(2, store.getFeedRelations(group).size());

        FeedRelation relation = new FeedRelation();
        relation.setId(4);
        store.delete(relation);

        assertEquals(1, store.getFeedRelations(group).size());
    }

    @Test
    public void Delete_InvalidRelation_WillSuccessful() {
        try {
            store.delete(null);
            fail("should throw");
        } catch (IllegalArgumentException e) {}

        try {
            FeedRelation relation = new FeedRelation();
            store.delete(relation);
            fail("should throw");
        } catch (IllegalArgumentException e) {}
    }

    @Test
    public void Update_ValidRelation_WillSuccess() {
        DBObject obj = collection.findOne(new BasicDBObject("id", 4));
        assertEquals("feed2", obj.get("name"));

        FeedRelation relation = new FeedRelation();
        relation.setId(4);
        relation.setFeedId(2);
        relation.setFeedId(2);
        relation.setName("hello");
        store.update(relation);

        assertEquals(5, collection.count());
        obj = collection.findOne(new BasicDBObject("id", 4));
        assertEquals("hello", obj.get("name"));

        relation.setId(9);
        store.update(relation);
        assertEquals(5, collection.count());
    }

    @Test
    public void Update_InvalidRelation_WillSuccess() {
        try {
            store.update(null);
            fail("should throw");
        } catch (IllegalArgumentException e) {}

        try {
            FeedRelation relation = new FeedRelation();
            store.update(relation);
            fail("should throw");
        } catch (IllegalArgumentException e) {}
    }
}
