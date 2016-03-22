package net.davidvoid.thor.lightning.data.access;

import java.util.List;

import net.davidvoid.thor.lightning.data.source.MongoDataSource;
import net.davidvoid.thor.lightning.entity.Feed;
import net.davidvoid.thor.lightning.entity.Item;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.DBCollection;

/**
 * Created by david on 3/21/16.
 */
public class ItemStore {
    @Autowired
    private MongoDataSource data_source = null;

    public List<Item> getItems(Feed feed) {
        return null;
    }

    public Item addItem(Item item) {
        return null;
    }

    public void updateItem(Item item) {

    }

    private DBCollection getCollection() {
        return data_source.getDatabase().getCollection("item");
    }

}
