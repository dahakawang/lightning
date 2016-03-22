package net.davidvoid.thor.lightning.entity;

import java.util.Date;

/**
 * Created by david on 3/20/16.
 */
public class Feed implements Entity {
    private long id = -1;
    private String name = "";
    private String description = "";
    private String url = "";
    private Date last_update = null;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

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

    public Date getLast_update() {
        return last_update;
    }

    public void setLast_update(Date last_update) {
        this.last_update = last_update;
    }

    @Override
    public boolean has_valid_id() {
        return this.id >= 0;
    }

}
