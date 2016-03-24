package net.davidvoid.thor.lightning.data.access;

import static org.springframework.util.Assert.notNull;

import java.util.Date;
import java.util.List;

import net.davidvoid.thor.lightning.entity.Entity;
import net.davidvoid.thor.lightning.entity.Feed;
import net.davidvoid.thor.lightning.entity.FeedRelation;

import org.springframework.stereotype.Component;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Created by david on 3/22/16. feed collection:
 * {_id: XX, id: long, description: string, url: string, last_update: Date}
 * index 1 : id, unique
 */
@Component
public class FeedStore extends AbstractStore {

    private static final String COLLECTION_NAME = "feed";

    @SuppressWarnings("unchecked")
    public List<Feed> getFeeds(List<FeedRelation> list) {
        notNull(list);

        DBObject query = get_query(list);
        return (List<Feed>) (List<?>) get(query);
    }

    private DBObject get_query(List<FeedRelation> list) {
        assert list != null;

        BasicDBList id_list = get_id_list(list);
        return new BasicDBObject("id", new BasicDBObject("$in", id_list));
    }

    private BasicDBList get_id_list(List<FeedRelation> list) {
        BasicDBList id_list = new BasicDBList();

        for (FeedRelation relation : list) {
            id_list.add(relation.getFeedId());
            assert relation.has_valid_id() : "the relation used to find feeds must has valid id";
        }

        return id_list;
    }

    @Override
    protected DBObject toDBObject(Entity entity) {
        assert entity != null;

        Feed feed = (Feed) entity;
        BasicDBObject object = new BasicDBObject("id", feed.getId());
        object.put("description", feed.getDescription());
        object.put("url", feed.getUrl());
        object.put("last_update", feed.getLastUpdate());

        return object;
    }

    @Override
    protected Entity toEntity(DBObject object) {
        assert object != null;

        Feed feed = new Feed();
        feed.setId((Long) object.get("id"));
        feed.setDescription((String) object.get("description"));
        feed.setUrl((String) object.get("url"));
        feed.setLastUpdate((Date) object.get("last_update"));

        return feed;
    }

    @Override
    protected DBObject getModifyQuery(Entity entity) {
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
