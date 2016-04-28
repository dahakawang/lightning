package net.davidvoid.thor.lightning.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by david on 3/20/16.
 */
public class User implements Entity {
    private long id = -1;
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

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean has_valid_id() {
        return this.id >= 0; 
    }

    @Override
    public void setInvalidId() {
        this.id = -1;
    }
}
