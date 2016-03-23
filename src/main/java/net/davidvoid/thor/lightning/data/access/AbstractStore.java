package net.davidvoid.thor.lightning.data.access;

import java.util.ArrayList;
import java.util.List;

import net.davidvoid.thor.lightning.data.source.MongoDataSource;
import net.davidvoid.thor.lightning.entity.Entity;
import net.davidvoid.thor.lightning.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.util.Assert.*;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public abstract class AbstractStore {
    @Autowired
    private MongoDataSource data_source = null;

    @Autowired
    private Counter counter = null;

    final public void add(Entity entity) {
        notNull(entity);
        isTrue(!entity.has_valid_id(),
                "the entity to be saved should not have a valid id");

        generateId(entity);
        DBObject object = toDBObject(entity);
        getCollection().insert(object);
    }

    final public void delete(Entity entity) {
        notNull(entity);
        isTrue(entity.has_valid_id(),
                "the entity to be deleted should have a valid id");

        DBObject query = getModifyQuery(entity);
        getCollection().remove(query);
        entity.setInvalidId();
    }

    final public void update(Entity entity) {
        notNull(entity);
        isTrue(entity.has_valid_id(),
                "the entity to be updated should have a valid id");

        DBObject query = getModifyQuery(entity);
        DBObject update = toDBObject(entity);
        getCollection().update(query, update, false, true);
    }

    final protected List<Entity> get(DBObject query) {
        assert query != null : "mongodb query should be be null";

        DBCursor cursor = getCollection().find(query);
        return retrieveAll(cursor);
    }

    final protected List<Entity> retrieveAll(DBCursor cursor) {
        ArrayList<Entity> list = new ArrayList<Entity>();
        while (cursor.hasNext()) {
            Entity entity = toEntity(cursor.next());
            list.add(entity);
        }
        return list;
    }

    final protected Entity getOne(DBObject query) {
        assert query != null : "mongodb query should be be null";

        DBObject object = getCollection().findOne(query);
        if (object == null)
            throw new ResourceNotFoundException("resource not found in db");

        return toEntity(object);
    }

    final protected long generateNextId(String name) {
        assert name != null : "next id collection name should not be null";
        assert !name.isEmpty() : "next id collection name should not be empty";

        return counter.getNextId(name);
    }

    final protected DBCollection getCollection() {
        assert getCollectionName() != null : "getCollectionName should not return null";
        assert !getCollectionName().isEmpty() : "getCollectionName should not return empty";

        return data_source.getDatabase().getCollection(getCollectionName());
    }

    private void generateId(Entity entity) {
        assert getCollectionName() != null : "getCollectionName should not return null";
        assert !getCollectionName().isEmpty() : "getCollectionName should not return empty";

        entity.setId(generateNextId(getCollectionName()));
    }

    protected abstract DBObject toDBObject(Entity entity);

    protected abstract Entity toEntity(DBObject object);

    protected abstract DBObject getModifyQuery(Entity entity);

    protected abstract String getCollectionName();
}
