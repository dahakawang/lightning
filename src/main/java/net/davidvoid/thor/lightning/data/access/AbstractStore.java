package net.davidvoid.thor.lightning.data.access;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import net.davidvoid.thor.lightning.entity.Entity;
import net.davidvoid.thor.lightning.exception.ResourceNotFoundException;

public abstract class AbstractStore {
    @Autowired
    private Counter counter = null;

    final public void add(Entity entity) {
        assert(!entity.has_valid_id());

        generateId(entity);
        DBObject object = toDBObject(entity);
        getCollection().insert(object);
    }
    
    final public void delete(Entity entity) {
        assert(entity.has_valid_id());
        
        DBObject query = getDeleteQuery(entity);
        getCollection().remove(query);
    }
    
    final public void update(Entity entity) {
        assert(entity.has_valid_id());
        
        DBObject query = getUpdateQuery(entity);
        DBObject update = toDBObject(entity);
        getCollection().update(query, update, false, true);
    }
    

    final protected List<Entity> get(DBObject query) {
        DBCursor cursor = getCollection().find(query);
        
        ArrayList<Entity> list = new ArrayList<Entity>();
        while(cursor.hasNext()) {
            Entity entity = toEntity(cursor.next());
            list.add(entity);
        }
        return list;
    }
    
    final protected Entity getOne(DBObject query) {
        DBObject object = getCollection().findOne(query);
        if (object == null) throw new ResourceNotFoundException("resource not found in db");

        return toEntity(object);
    }
    
    final protected long generateNextId(String name) {
        return counter.getNextId(name);
    }
    
    protected abstract DBObject toDBObject(Entity entity);
    protected abstract Entity toEntity(DBObject object);
    protected abstract DBCollection getCollection();
    protected abstract void generateId(Entity entity);
    protected abstract DBObject getDeleteQuery(Entity entity);
    protected abstract DBObject getUpdateQuery(Entity entity);
}
