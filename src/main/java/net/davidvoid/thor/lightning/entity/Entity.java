package net.davidvoid.thor.lightning.entity;

public interface Entity {
    /**
     * Every entity saved to DB will have a unique ID assigned, so if 
     * the entity has a valid id, then we can assumes this entity has already
     * been saved to DB.
     */
    boolean has_valid_id();
    long getId();
    void setId(long id);
    void setInvalidId();
}
