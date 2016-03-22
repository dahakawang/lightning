package net.davidvoid.thor.lightning.data.access;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.davidvoid.thor.lightning.data.source.MongoDataSource;
import net.davidvoid.thor.lightning.entity.Entity;
import net.davidvoid.thor.lightning.entity.Feed;
import net.davidvoid.thor.lightning.entity.FeedRelation;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Created by david on 3/22/16. feed collection: {_id: XX, id: long,
 * description: string, url: string, last_update: Date} index 1 : id, unique
 */
public class FeedStore extends AbstractStore {

    private static final String COLLECTION_NAME = "feed";
    @Autowired
    private MongoDataSource data_source = null;
    @Autowired
    private Counter counter = null;

    @SuppressWarnings("unchecked")
    public List<Feed> getFeed(List<FeedRelation> list) {
        DBObject query = get_query(list);
        return (List<Feed>) (List<?>) get(query);
    }

    private DBObject get_query(List<FeedRelation> list) {
        BasicDBList id_list = get_id_list(list);
        BasicDBObject query = new BasicDBObject("id", new BasicDBObject("$in",
                id_list));
        return query;
    }

    private BasicDBList get_id_list(List<FeedRelation> list) {
        BasicDBList id_list = new BasicDBList();
        Iterator<FeedRelation> it = list.iterator();

        while (it.hasNext()) {
            id_list.add(it.next().getFeed_id());
        }

        return id_list;
    }

    @Override
    protected DBObject toDBObject(Entity entity) {
        Feed feed = (Feed) entity;

        BasicDBObject object = new BasicDBObject("id", feed.getId());
        object.put("description", feed.getDescription());
        object.put("url", feed.getUrl());
        object.put("last_update", feed.getLast_update());

        return object;
    }

    @Override
    protected Entity toEntity(DBObject object) {
        Feed feed = new Feed();
        feed.setId((Long) object.get("id"));
        feed.setDescription((String) object.get("description"));
        feed.setUrl((String) object.get("url"));
        feed.setLast_update((Date) object.get("last_update"));

        return feed;
    }

    @Override
    protected DBObject getModifyQuery(Entity entity) {
        Feed feed = (Feed) entity;
        return new BasicDBObject("id", feed.getId());
    }

    @Override
    protected String getCollectionName() {
        return COLLECTION_NAME;
    }
}
