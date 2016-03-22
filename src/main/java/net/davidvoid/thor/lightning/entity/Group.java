package net.davidvoid.thor.lightning.entity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by david on 3/20/16.
 */
public class Group {
    private long id = -1;
    private String name = "";
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
    
    public List<Long> getFeeds() {
		return feeds;
	}

	public void setFeeds(List<Long> feeds) {
		this.feeds = feeds;
	}

	public boolean has_valid_id() {
    	return id != -1;
    }
}
