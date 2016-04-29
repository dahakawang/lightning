package net.davidvoid.thor.lightning.entity;

/**
 * Created by david on 3/20/16.
 */
public class FeedRelation extends Entity {
    private long group_id = -1;
    private long feed_id = -1;
    private String name = null;
    private Group group = null;

    public long getGroupId() {
        return group_id;
    }

    public void setGroupId(long group_id) {
        this.group_id = group_id;
    }

    public long getFeedId() {
        return feed_id;
    }

    public void setFeedId(long feed_id) {
        this.feed_id = feed_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
