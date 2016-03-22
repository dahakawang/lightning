package net.davidvoid.thor.lightning.data.access;

import java.util.List;

import net.davidvoid.thor.lightning.entity.Entity;
import net.davidvoid.thor.lightning.entity.FeedRelation;
import net.davidvoid.thor.lightning.entity.Group;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Created by david on 3/21/16.
 * feed_relation collection:
 * {Id: XX, group_id: long, feed_id: long, id: long, name: string}
 * index 1 : group_id, feed_id, unique
 */
public class FeedRelationStore extends AbstractStore {
    
    private static final String COLLECTION_NAME = "feed_relation";

    @SuppressWarnings("unchecked")
    public List<FeedRelation> getFeedRelations(Group group) {
    	assert(group.has_valid_id());

        BasicDBObject query = new BasicDBObject("group_id", group.getId());
        return (List<FeedRelation>) (List<?>) get(query);
    }

    @Override
    protected DBObject toDBObject(Entity entity) {
        FeedRelation feed = (FeedRelation) entity;

    	BasicDBObject object = new BasicDBObject("id", feed.getId());
    	object.put("group_id", feed.getGroupId());
    	object.put("feed_id", feed.getFeedId());
    	object.put("name", feed.getName());

		return object;
    }

    @Override
    protected Entity toEntity(DBObject object) {
		FeedRelation relation = new FeedRelation();
		relation.setId((Long)object.get("id"));
        relation.setGroupId((Long) object.get("group_id"));
        relation.setFeedId((Long) object.get("feed_id"));
		relation.setName((String)object.get("name"));
		
		return relation;
    }

    @Override
    protected DBObject getModifyQuery(Entity entity) {
        FeedRelation feed = (FeedRelation) entity;
        
    	BasicDBObject object = new BasicDBObject("group_id", feed.getGroupId());
    	object.put("feed_id", feed.getFeedId());
        return object;
    }

    @Override
    protected String getCollectionName() {
        return COLLECTION_NAME;
    }
}
