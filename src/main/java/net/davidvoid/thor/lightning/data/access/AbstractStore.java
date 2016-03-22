package net.davidvoid.thor.lightning.data.access;

import java.util.ArrayList;
import java.util.List;

import net.davidvoid.thor.lightning.data.source.MongoDataSource;
import net.davidvoid.thor.lightning.entity.Entity;
import net.davidvoid.thor.lightning.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public abstract class AbstractStore {
    @Autowired
    private MongoDataSource data_source = null;

    @Autowired
    private Counter counter = null;

    final public void add(Entity entity) {
        assert (!entity.has_valid_id());

        generateId(entity);
        DBObject object = toDBObject(entity);
        getCollection().insert(object);
    }

    final public void delete(Entity entity) {
        assert (entity.has_valid_id());

        DBObject query = getModifyQuery(entity);
        getCollection().remove(query);
    }

    final public void update(Entity entity) {
        assert (entity.has_valid_id());

        DBObject query = getModifyQuery(entity);
        DBObject update = toDBObject(entity);
        getCollection().update(query, update, false, true);
    }

    final protected List<Entity> get(DBObject query) {
        DBCursor cursor = getCollection().find(query);

        ArrayList<Entity> list = new ArrayList<Entity>();
        while (cursor.hasNext()) {
            Entity entity = toEntity(cursor.next());
            list.add(entity);
        }
        return list;
    }

    final protected Entity getOne(DBObject query) {
        DBObject object = getCollection().findOne(query);
        if (object == null)
            throw new ResourceNotFoundException("resource not found in db");

        return toEntity(object);
    }

    final protected long generateNextId(String name) {
        return counter.getNextId(name);
    }

    final protected DBCollection getCollection() {
        return data_source.getDatabase().getCollection(getCollectionName());
    }

    protected abstract DBObject toDBObject(Entity entity);

    protected abstract Entity toEntity(DBObject object);

    private void generateId(Entity entity) {
        entity.setId(generateNextId(getCollectionName()));
    }

    protected abstract DBObject getModifyQuery(Entity entity);

    protected abstract String getCollectionName();
}
