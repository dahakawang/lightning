package net.davidvoid.thor.lightning.entity;

/**
 * Created by david on 3/20/16.
 */
public class FeedRelation implements Entity {
    private long id = -1;
    private long group_id = -1;
    private long feed_id = -1;
    private String name = null;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(long group_id) {
        this.group_id = group_id;
    }

    public long getFeed_id() {
        return feed_id;
    }

    public void setFeed_id(long feed_id) {
        this.feed_id = feed_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean has_valid_id() {
        return id >= 0;
    }
}
