package net.davidvoid.thor.lightning.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.davidvoid.thor.lightning.data.access.FeedStore;
import net.davidvoid.thor.lightning.data.access.GroupStore;
import net.davidvoid.thor.lightning.data.access.ItemStore;
import net.davidvoid.thor.lightning.data.access.ItemStore.FILTER_OPTION;
import net.davidvoid.thor.lightning.entity.Feed;
import net.davidvoid.thor.lightning.entity.Group;
import net.davidvoid.thor.lightning.entity.Item;

@Component
public class ItemService {
    @Autowired
    ItemStore store = null;
    @Autowired
    GroupStore group_store = null;
    @Autowired
    FeedStore feed_store =null;
    
    public List<Item> get(Object group_id, Object feed_id, boolean onlyUnread, ItemStore.SORT_OPTION sortBy, int start, int count) {
        FILTER_OPTION filterOption = onlyUnread? FILTER_OPTION.ONLY_UNREAD : FILTER_OPTION.BOTH;
        
        Group group = group_store.getGroupById(group_id);
        Feed feed = feed_store.getFeedByGroupAndFeedIds(group, feed_id);

        return store.getItemsFromFeed(feed, start, count, filterOption, sortBy);
    }
    
    public long count(Object group_id, Object feed_id, boolean onlyUnread) {
        Group group = group_store.getGroupById(group_id);
        Feed feed = feed_store.getFeedByGroupAndFeedIds(group, feed_id);

        return store.count(feed);
    }

}
