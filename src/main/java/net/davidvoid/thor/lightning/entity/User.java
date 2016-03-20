package net.davidvoid.thor.lightning.entity;

/**
 * Created by david on 3/20/16.
 */
public class User {
    private long id = -1;
    private String name = "";
    private String password = "";

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
