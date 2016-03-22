package net.davidvoid.thor.lightning.data.access;

import java.util.List;

import net.davidvoid.thor.lightning.data.source.MongoDataSource;
import net.davidvoid.thor.lightning.entity.Feed;
import net.davidvoid.thor.lightning.entity.Group;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.DBCollection;

/**
 * Created by david on 3/21/16.
 */
public class FeedStore {
    @Autowired
    private MongoDataSource data_source = null;

    public List<Feed> getFeeds(Group group) {
        return null;
    }

    public Feed addFeed(Feed feed) {
        return null;
    }

    public void updateFeed(Feed feed) {

    }

    private DBCollection getCollection() {
        return data_source.getDatabase().getCollection("feed");
    }
}
