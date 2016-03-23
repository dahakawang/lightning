package net.davidvoid.thor.lightning.entity;


/**
 * Created by david on 3/20/16.
 */
public class Group implements Entity {
    private long id = -1;
    private String name = "";
    private User user = null;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean has_valid_id() {
        return id >= 0;
    }

    @Override
    public void setInvalidId() {
        this.id = -1;
    }
}
