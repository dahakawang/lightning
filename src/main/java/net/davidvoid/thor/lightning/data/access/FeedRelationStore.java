package net.davidvoid.thor.lightning.data.access;

import java.util.List;

import net.davidvoid.thor.lightning.entity.Entity;
import net.davidvoid.thor.lightning.entity.FeedRelation;
import net.davidvoid.thor.lightning.entity.Group;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Component;

import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

import com.mongodb.BasicDBObject;

/**
 * Created by david on 3/21/16.
 * feed_relation collection:
 * {Id: XX, group_id: long, feed_id: long, id: long, name: string}
 * index 1 : group_id, feed_id, unique
 */
@Component
public class FeedRelationStore extends AbstractStore {
    
    private static final String COLLECTION_NAME = "feed_relation";

    @SuppressWarnings("unchecked")
    public List<FeedRelation> getFeedRelations(Group group) {
        notNull(group);
        isTrue(group.has_valid_id());

        BasicDBObject query = new BasicDBObject("group_id", group.getId());
        return inject_group((List<FeedRelation>) (List<?>) get(query), group);
    }

    private List<FeedRelation> inject_group(List<FeedRelation> list, Group group) {
        for (FeedRelation relation : list) {
            relation.setGroup(group);
        }
        return list;
    }

    @Override
    protected Document toDocument(Entity entity) {
        FeedRelation feed = (FeedRelation) entity;

    	Document object = new Document("id", feed.getId());
    	object.put("group_id", feed.getGroupId());
    	object.put("feed_id", feed.getFeedId());
    	object.put("name", feed.getName());

		return object;
    }

    @Override
    protected Entity toEntity(Document object) {
		FeedRelation relation = new FeedRelation();
		relation.setId((Long)object.get("id"));
        relation.setGroupId((Long) object.get("group_id"));
        relation.setFeedId((Long) object.get("feed_id"));
		relation.setName((String)object.get("name"));
		
		return relation;
    }

    @Override
    protected Bson getModifyQuery(Entity entity) {
        FeedRelation feed = (FeedRelation) entity;

        return new BasicDBObject("id", feed.getId());
    }

    @Override
    protected String getCollectionName() {
        return COLLECTION_NAME;
    }
}
