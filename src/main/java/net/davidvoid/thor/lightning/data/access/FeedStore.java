package net.davidvoid.thor.lightning.data.access;

import static org.springframework.util.Assert.isTrue;
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

        Bson query = get_query(group.getId());
        return inject_group((List<Feed>) (List<?>) get(query), group);
    }

    public Feed getFeedByGroupId(Object group_id) {
        notNull(group_id);
        isTrue(Entity.is_valid_id(group_id));

        Bson query = get_query(group_id);
        return (Feed) getOne(query);
    }

    public Feed getFeedByGroupAndFeedIds(Group group, Object feed_id) {
        notNull(group.getId());
        notNull(feed_id);
        isTrue(group.has_valid_id());
        isTrue(Entity.is_valid_id(feed_id));

        BasicDBObject query = new BasicDBObject("id", feed_id);
        query.put("group_id", group.getId());
        Feed feed = (Feed) getOne(query);
        feed.setGroup(group);
        return feed;
    }

    public Feed getFeedByIds(Object id) {
        notNull(id);
        isTrue(Entity.is_valid_id(id));

        Bson query = new BasicDBObject("id", id);
        return (Feed) getOne(query);
    }

    public long countFeedsByGroupId(Object group_id) {
        notNull(group_id);
        isTrue(Entity.is_valid_id(group_id));

        Bson query = get_query(group_id);
        return count(query);
    }

    private List<Feed> inject_group(List<Feed> list, Group group) {
        for (Feed feed : list) {
            feed.setGroup(group);
        }
        return list;
    }

    private Bson get_query(Object group_id) {
        assert group_id != null;

        return new BasicDBObject("group_id", group_id);
    }

    @Override
    protected Document toDocument(Entity entity) {
        assert entity != null;

        Feed feed = (Feed) entity;
        Document object = new Document("id", feed.getId());
        object.put("group_id", feed.getGroup().getId());
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
