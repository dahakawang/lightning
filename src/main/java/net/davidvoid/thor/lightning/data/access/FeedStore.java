package net.davidvoid.thor.lightning.data.access;

import static org.springframework.util.Assert.notNull;

import java.util.Date;
import java.util.List;

import net.davidvoid.thor.lightning.entity.Entity;
import net.davidvoid.thor.lightning.entity.Feed;
import net.davidvoid.thor.lightning.entity.Group;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;

/**
 * Created by david on 3/22/16. feed collection:
 * {_id: XX, id: long, group_id: long, name: string, description: string, url: string, last_update: Date}
 * index 1 : id, unique
 */
@Component
public class FeedStore extends AbstractStore {

    private static final String COLLECTION_NAME = "feed";

    @SuppressWarnings("unchecked")
    public List<Feed> getFeeds(Group group) {
        notNull(group);

        Bson query = get_query(group);
        return inject_group((List<Feed>) (List<?>) get(query), group);
    }

    private List<Feed> inject_group(List<Feed> list, Group group) {
        for (Feed feed : list) {
            feed.setGroup(group);
        }
        return list;
    }

    private Bson get_query(Group group) {
        assert group != null;

        return new BasicDBObject("group_id", group.getId()); 
    }

    @Override
    protected Document toDocument(Entity entity) {
        assert entity != null;

        Feed feed = (Feed) entity;
        Document object = new Document("id", feed.getId());
        object.put("description", feed.getDescription());
        object.put("url", feed.getUrl());
        object.put("last_update", feed.getLastUpdate());
    	object.put("name", feed.getName());

        return object;
    }

    @Override
    protected Entity toEntity(Document object) {
        assert object != null;

        Feed feed = new Feed();
        feed.setId((Long) object.get("id"));
        feed.setDescription((String) object.get("description"));
        feed.setUrl((String) object.get("url"));
        feed.setLastUpdate((Date) object.get("last_update"));
		feed.setName((String)object.get("name"));

        return feed;
    }

    @Override
    protected Bson getModifyQuery(Entity entity) {
        assert entity != null;

        Feed feed = (Feed) entity;
        assert feed.has_valid_id();
        return new BasicDBObject("id", feed.getId());
    }

    @Override
    protected String getCollectionName() {
        return COLLECTION_NAME;
    }
}
