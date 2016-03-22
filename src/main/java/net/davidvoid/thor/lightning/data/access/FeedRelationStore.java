package net.davidvoid.thor.lightning.data.access;

import java.util.ArrayList;
import java.util.List;

import net.davidvoid.thor.lightning.data.source.MongoDataSource;
import net.davidvoid.thor.lightning.entity.FeedRelation;
import net.davidvoid.thor.lightning.entity.Group;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * Created by david on 3/21/16.
 * feed_relation collection:
 * {Id: XX, group_id: long, feed_id: long, name: string}
 * index 1 : group_id, feed_id, unique
 */
public class FeedRelationStore {
    @Autowired
    private MongoDataSource data_source = null;

    public List<FeedRelation> getFeedRelations(Group group) {
    	assert(group.has_valid_id());
    	
    	BasicDBObject query = get_query(group);
    	DBCursor cursor = getCollection().find(query);
    	
    	return get_all(cursor);
    }

    private List<FeedRelation> get_all(DBCursor cursor) {
    	ArrayList<FeedRelation> relation = new ArrayList<FeedRelation>();
    	
    	while(cursor.hasNext()) {
    		DBObject object = cursor.next();
    		relation.add(to_relation(object));
    	}
    	return relation;
	}

	private FeedRelation to_relation(DBObject object) {
		FeedRelation relation = new FeedRelation();
		relation.setGroup_id((Long)object.get("group_id"));
		relation.setFeed_id((Long)object.get("feed_id"));
		relation.setName((String)object.get("name"));
		
		return relation;
	}

	private BasicDBObject get_query(Group group) {
		BasicDBObject query = new BasicDBObject("group_id", group.getId());
		return query;
	}

	public FeedRelation addFeedRelation(FeedRelation feed) {
		DBObject object = to_object(feed);
		getCollection().insert(object);

		return feed;
    }

    private DBObject to_object(FeedRelation feed) {
    	BasicDBObject object = new BasicDBObject("group_id", feed.getGroup_id());
    	object.put("feed_id", feed.getFeed_id());
    	object.put("name", feed.getName());

		return object;
	}

	public void updateFeedRelation(FeedRelation feed) {
		BasicDBObject query = get_query(feed);
		DBObject update = to_object(feed);

		getCollection().update(query, update);
    }


    private BasicDBObject get_query(FeedRelation feed) {
    	BasicDBObject object = new BasicDBObject("group_id", feed.getGroup_id());
    	object.put("feed_id", feed.getFeed_id());

		return object;
	}

	private DBCollection getCollection() {
        return data_source.getDatabase().getCollection("feed_relation");
    }
}
