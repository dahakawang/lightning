package net.davidvoid.thor.lightning.entity;


/**
 * Created by david on 3/20/16.
 */
public class Group extends Entity {
    private String name = "";
    private User user = null;

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
}
