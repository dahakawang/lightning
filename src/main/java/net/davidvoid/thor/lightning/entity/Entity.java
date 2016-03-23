package net.davidvoid.thor.lightning.entity;

public interface Entity {
    /**
     * Every entity saved to DB will have a unique ID assigned, so if 
     * the entity has a valid id, then we can assumes this entity has already
     * been saved to DB.
     */
    public boolean has_valid_id();
    public long getId();
    public void setId(long id);
    public void setInvalidId();
}
