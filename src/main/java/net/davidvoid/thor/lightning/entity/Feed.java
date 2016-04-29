package net.davidvoid.thor.lightning.entity;

import java.util.Date;

/**
 * Created by david on 3/20/16.
 */
public class Feed extends Entity {
    private String name = "";
    private String description = "";
    private String url = "";
    private Date last_update = null;
    private FeedRelation relation = null;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getLastUpdate() {
        return last_update;
    }

    public void setLastUpdate(Date last_update) {
        this.last_update = last_update;
    }

    public FeedRelation getRelation() {
        return relation;
    }

    public void setRelation(FeedRelation relation) {
        this.relation = relation;
    }

}
