package net.davidvoid.thor.lightning.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by david on 3/20/16.
 */
public class User extends Entity {
    private String name = "";
    @JsonIgnore
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
}
