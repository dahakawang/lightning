package net.davidvoid.thor.lightning.data.access;

import java.util.List;

import net.davidvoid.thor.lightning.data.source.MongoDataSource;
import net.davidvoid.thor.lightning.entity.FeedRelation;
import net.davidvoid.thor.lightning.entity.Group;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.DBCollection;

/**
 * Created by david on 3/22/16.
 * feed collection:
 * {_id: XX, id: long, description: string, url: string, last_update: Date}
 * index 1 : id, unique
 */
public class FeedStore {
    @Autowired
    private MongoDataSource data_source = null;

    public List<FeedRelation> getFeedRelations(Group group) {
        return null;
    }

    public FeedRelation addFeedRelation(FeedRelation feed) {
        return null;
    }

    public void updateFeedRelation(FeedRelation feed) {

    }

    private DBCollection getCollection() {
        return data_source.getDatabase().getCollection("feed");
    }
}
