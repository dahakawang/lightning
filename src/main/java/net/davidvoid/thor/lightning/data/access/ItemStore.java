package net.davidvoid.thor.lightning.data.access;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import net.davidvoid.thor.lightning.entity.Entity;
import net.davidvoid.thor.lightning.entity.Feed;
import net.davidvoid.thor.lightning.entity.Item;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;


/**
 * Created by david on 3/21/16.
 * collection name: item
 * { _id: XX, id: long, name: string, author: string, content: string,
 * url: string, is_saved: boolean, is_read: boolean, last_update; Date}
 */
@Component
public class ItemStore extends AbstractStore {

    private static final String COLLECTION_NAME = "item";
    
    public List<Item> getItemsFromFeed(Feed feed) {
        assert(feed.has_valid_id());
        
        return null;
    }

    @Override
    protected DBObject toDBObject(Entity entity) {
        Item item = (Item) entity;
        
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
        Item item = (Item) entity;
        return new BasicDBObject("id", item.getId());
    }

    @Override
    protected String getCollectionName() {
        return COLLECTION_NAME;
    }
}
