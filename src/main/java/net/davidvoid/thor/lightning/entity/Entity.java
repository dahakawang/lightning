package net.davidvoid.thor.lightning.entity;

import static org.springframework.util.Assert.*;

public abstract class Entity {
    Long id = null;

    /**
     * Every entity saved to DB will have a unique ID assigned, so if 
     * the entity has a valid id, then we can assumes this entity has already
     * been saved to DB.
     */
    public boolean has_valid_id() {
        return id != null;
    }

    public Object getId() {
        isTrue(has_valid_id());
        
        return id;
    }
    
    public void setId(long id) {
        this.id = (Long) id;
    }

    public void setId(Object id) {
        notNull(id);
        isInstanceOf(Long.class, id);
        
        this.id = (Long) id;
    }

    public void setInvalidId() {
        id = null;
    }
    
    public static boolean is_valid_id(Object id) {
        return id != null && (id instanceof Long);
    }
}
