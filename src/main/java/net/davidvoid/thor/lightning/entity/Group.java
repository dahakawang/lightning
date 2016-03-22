package net.davidvoid.thor.lightning.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 3/20/16.
 */
public class Group implements Entity {
    private long id = -1;
    private String name = "";
    private User user = null;
    private List<Long> feeds = new ArrayList<Long>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Long> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<Long> feeds) {
        this.feeds = feeds;
    }

    @Override
    public boolean has_valid_id() {
        return id >= 0;
    }
}
