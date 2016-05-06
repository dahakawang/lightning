package net.davidvoid.thor.lightning.service;

import java.util.Date;
import java.util.List;

import net.davidvoid.thor.lightning.data.access.FeedStore;
import net.davidvoid.thor.lightning.data.access.GroupStore;
import net.davidvoid.thor.lightning.entity.Feed;
import net.davidvoid.thor.lightning.entity.Group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by david on 4/30/16.
 */
@Component
public class FeedService {

    @Autowired
    FeedStore feed_store = null;
    @Autowired
    GroupStore group_store = null;

    public List<Feed> getFeeds(Object group_id) {
        Group group = group_store.getGroupById(group_id);
        return feed_store.getFeeds(group);
    }

    public Feed add(Object group_id, Feed feed) {
        Group group = group_store.getGroupById(group_id);
        feed.setGroup(group);
        feed.setLastUpdate(new Date());

        feed_store.add(feed);
        return feed;
    }

    public Feed update(Object group_id, Feed feed) {
        Group group = group_store.getGroupById(group_id);
        Feed original_feed = feed_store.getFeedByGroupAndFeedIds(group, feed.getId());
        original_feed.setName(feed.getName());
        original_feed.setDescription(feed.getDescription());

        feed_store.update(original_feed);
        return original_feed;
    }

    public void delete(Object group_id, Object id) {
        // The items under this feed will be deleted by the background worker
        Group group = group_store.getGroupById(group_id);
        Feed original_feed = feed_store.getFeedByGroupAndFeedIds(group, id);

        feed_store.delete(original_feed);
    }

}
