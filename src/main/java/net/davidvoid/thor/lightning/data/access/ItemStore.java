package net.davidvoid.thor.lightning.data.access;

import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

import java.util.Date;
import java.util.List;

import net.davidvoid.thor.lightning.entity.Entity;
import net.davidvoid.thor.lightning.entity.Feed;
import net.davidvoid.thor.lightning.entity.Item;

import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;


/**
 * Created by david on 3/21/16.
 * collection name: item
 * { _id: XX, feed_id: long, id: long, name: string, author: string, content: string, 
 * url: string, is_saved: boolean, is_read: boolean, last_update; Date}
 * index 1: id
 * index 2: feed_id, name
 * index 3: feed_id, last_update
 * index 4: feed_id, is_read, last_update
 * index 5: feed_id, is_read, name
 */
@Component
public class ItemStore extends AbstractStore {

    public enum FILTER_OPTION { ONLY_READ, ONLY_UNREAD, BOTH }
    public enum SORT_OPTION { BY_UPDATE_DATE_ASC, BY_UPDATE_DATE_DSC,
        BY_NAME_ASC, BY_NAME_DSC}

    private static final String COLLECTION_NAME = "item";



    @SuppressWarnings("unchecked")
    public List<Item> getItemsFromFeed(Feed feed) {
        notNull(feed);
        isTrue(feed.has_valid_id());
        
        BasicDBObject query = new BasicDBObject("feed_id", feed.getId());
        BasicDBObject order = new BasicDBObject("last_update", 1);
        return inject_feed((List<Item>) (List<?>) get(query, order), feed);
    }

    public long count(Feed feed) {
        notNull(feed);
        isTrue(feed.has_valid_id());

        BasicDBObject query = new BasicDBObject("id", feed.getId());
        return count(query);
    }

    @SuppressWarnings("unchecked")
    public List<Item> getItemsFromFeed(Feed feed, int offset, int count, FILTER_OPTION filter_option, SORT_OPTION sort_option) {
        notNull(feed);
        isTrue(feed.has_valid_id());
        BasicDBObject query = getQuery(feed, filter_option);
        BasicDBObject order = getOrder(sort_option);

        return inject_feed((List<Item>) (List<?>) get(query, order, offset, count), feed);
    }



    private List<Item> inject_feed(List<Item> list, Feed feed) {
        for (Item item : list) {
            item.setFeed(feed);
        }
        return list;
    }

    private BasicDBObject getOrder(SORT_OPTION sort_option) {
        switch (sort_option){
            case BY_NAME_ASC:
                return new BasicDBObject("name", 1);
            case BY_NAME_DSC:
                return new BasicDBObject("name", -1);
            case BY_UPDATE_DATE_ASC:
                return new BasicDBObject("last_update", 1);
            case BY_UPDATE_DATE_DSC:
                return new BasicDBObject("last_update", -1);
            default:
                assert false;
                return null;
        }
    }

    private BasicDBObject getQuery(Feed feed, FILTER_OPTION filter_option) {
        assert feed != null;
        assert feed.has_valid_id();

        BasicDBObject object = new BasicDBObject("id", feed.getId());
        if (filter_option == FILTER_OPTION.ONLY_READ) {
            object.put("is_read", true);
        }
        if (filter_option == FILTER_OPTION.ONLY_UNREAD) {
            object.put("is_read", false);
        }
        return object;
    }

    @Override
    protected DBObject toDBObject(Entity entity) {
        assert entity != null;
        Item item = (Item) entity;
        assert item.has_valid_id();
        
        BasicDBObject object = new BasicDBObject("id", item.getId());
        object.put("name", item.getName());
        object.put("author", item.getAuthor());
        object.put("content", item.getContent());
        object.put("url", item.getUrl());
        object.put("is_saved", item.isSaved());
        object.put("is_read", item.isRead());
        object.put("last_update", item.getLastUpdate());

        return object;
    }

    @Override
    protected Entity toEntity(DBObject object) {
        assert object != null;
        
        Item item = new Item();
        item.setId((Long)object.get("id"));
        item.setName((String)object.get("name"));
        item.setAuthor((String)object.get("author"));
        item.setContent((String)object.get("content"));
        item.setUrl((String)object.get("url"));
        item.setIsSaved((boolean)object.get("is_saved"));
        item.setIsRead((boolean)object.get("is_read"));
        item.setLastUpdate((Date)object.get("last_update"));
        
        return item;
    }

    @Override
    protected DBObject getModifyQuery(Entity entity) {
        assert entity != null;
        
        Item item = (Item) entity;
        assert item.has_valid_id();
        return new BasicDBObject("id", item.getId());
    }

    @Override
    protected String getCollectionName() {
        return COLLECTION_NAME;
    }
}
