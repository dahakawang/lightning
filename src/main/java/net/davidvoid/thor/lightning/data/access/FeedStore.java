package net.davidvoid.thor.lightning.data.access;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.davidvoid.thor.lightning.data.source.MongoDataSource;
import net.davidvoid.thor.lightning.entity.Feed;
import net.davidvoid.thor.lightning.entity.FeedRelation;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * Created by david on 3/22/16.
 * feed collection:
 * {_id: XX, id: long, description: string, url: string, last_update: Date}
 * index 1 : id, unique
 */
public class FeedStore {
    @Autowired
    private MongoDataSource data_source = null;
    @Autowired
    private Counter counter = null;

    public List<Feed> getFeed(List<FeedRelation> list) {
        DBObject object = get_query(list);
        DBCursor cursor = getCollection().find(object);

        return get_all(cursor);
    }

    private List<Feed> get_all(DBCursor cursor) {
        ArrayList<Feed> list = new ArrayList<Feed>();
        
        while(cursor.hasNext()) {
            Feed feed = to_feed(cursor.next());
            list.add(feed);
        }
        return list;
    }

    private Feed to_feed(DBObject object) {
        Feed feed = new Feed();
        feed.setId((Long)object.get("id"));
        feed.setDescription((String)object.get("description"));
        feed.setUrl((String)object.get("url"));
        feed.setLast_update((Date)object.get("last_update"));

        return feed;
    }

    private DBObject get_query(List<FeedRelation> list) {
        BasicDBList id_list = get_id_list(list);
        BasicDBObject query = new BasicDBObject("id", new BasicDBObject("$in", id_list));
        return query;
    }

    private BasicDBList get_id_list(List<FeedRelation> list) {
        BasicDBList id_list = new BasicDBList();
        Iterator<FeedRelation> it = list.iterator();
        
        while(it.hasNext()) {
            id_list.add(it.next().getFeed_id());
        }

        return id_list;
    }

    public Feed addFeed(Feed feed) {
        long id = nextId();
        feed.setId(id);

        DBObject object = to_object(feed);
        getCollection().insert(object);

        return feed;
    }

    private long nextId() {
        return counter.getNextId("feed");
    }

    private DBObject to_object(Feed feed) {
        BasicDBObject object = new BasicDBObject("id", feed.getId());
        object.put("description", feed.getDescription());
        object.put("url", feed.getUrl());
        object.put("last_update", feed.getLast_update());

        return object;
    }

    public void updateFeed(Feed feed) {
        DBObject query = get_query(feed);
        DBObject update = to_object(feed);
        getCollection().update(query, update);
    }

    private DBObject get_query(Feed feed) {
        return new BasicDBObject("id", feed.getId());
    }

    private DBCollection getCollection() {
        return data_source.getDatabase().getCollection("feed");
    }
}
