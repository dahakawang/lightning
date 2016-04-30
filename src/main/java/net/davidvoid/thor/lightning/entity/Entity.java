package net.davidvoid.thor.lightning.entity;

import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

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
        
        if (id instanceof Long) {
            this.id = (Long) id;
        } else if (id instanceof Integer) {
            this.id = ((Integer)id).longValue();
        } else {
            throw new IllegalArgumentException("id should only be Long or Integer");
        }
    }

    public void setInvalidId() {
        id = null;
    }
    
    public static boolean is_valid_id(Object id) {
        return id != null && ((id instanceof Integer) || (id instanceof Long));
    }
}
