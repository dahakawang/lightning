package net.davidvoid.thor.lightning.entity;

import java.util.Date;

/**
 * Created by david on 3/20/16.
 */
public class Item implements Entity {
    private long id = -1;
    private FeedRelation feed = null;
    private String name = "";
    private String author = "";
    private String content = "";
    private String url = "";
    private boolean is_saved = false;
    private boolean is_read = false;
    private Date last_update = null;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public FeedRelation getFeed() {
        return feed;
    }

    public void setFeed(FeedRelation feed) {
        this.feed = feed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSaved() {
        return is_saved;
    }

    public void setIsSaved(boolean is_saved) {
        this.is_saved = is_saved;
    }

    public boolean isRead() {
        return is_read;
    }

    public void setIsRead(boolean is_read) {
        this.is_read = is_read;
    }

    public Date getLastUpdate() {
        return last_update;
    }

    public void setLastUpdate(Date last_update) {
        this.last_update = last_update;
    }

    @Override
    public boolean has_valid_id() {
        return this.id >= 0;
    }
}
