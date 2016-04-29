package net.davidvoid.thor.lightning.data.access;

import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

import java.util.ArrayList;
import java.util.List;

import net.davidvoid.thor.lightning.data.source.MongoDataSource;
import net.davidvoid.thor.lightning.entity.Entity;
import net.davidvoid.thor.lightning.exception.ResourceNotFoundException;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.UpdateOptions;

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
        Document object = toDocument(entity);
        getCollection().insertOne(object);
    }
    
    final public void deleteById(Object id) {
        isTrue(Entity.is_valid_id(id));
        
        Bson query = new BasicDBObject("id", id);
        getCollection().deleteOne(query);
    }

    final public void delete(Entity entity) {
        notNull(entity);
        isTrue(entity.has_valid_id(),
                "the entity to be deleted should have a valid id");

        Bson query = getModifyQuery(entity);
        getCollection().deleteOne(query);
        entity.setInvalidId();
    }

    final public void update(Entity entity) {
        notNull(entity);
        isTrue(entity.has_valid_id(),
                "the entity to be updated should have a valid id");

        Bson query = getModifyQuery(entity);
        Document update = toDocument(entity);
        UpdateOptions options = new UpdateOptions();
        options.upsert(false);
        getCollection().replaceOne(query, update, options);
    }

    final protected long count(Bson query) {
        assert query != null : "mongodb query should be be null";

        return getCollection().count(query);
    }


    final protected List<Entity> get(Bson query, Bson sort, int offset, int count) {
        assert query != null : "mongodb query should be be null";

        MongoCursor<Document> cursor = getCollection().find(query).sort(sort).skip(offset).limit(count).iterator();
        return retrieveAll(cursor);
    }

    final protected List<Entity> get(Bson query, Bson sort) {
        assert query != null : "mongodb query should be be null";

        MongoCursor<Document> cursor = getCollection().find(query).sort(sort).iterator();
        return retrieveAll(cursor);
    }

    final protected List<Entity> get(Bson query) {
        assert query != null : "mongodb query should be be null";

        MongoCursor<Document> cursor = getCollection().find(query).iterator();
        return retrieveAll(cursor);
    }

    final protected List<Entity> retrieveAll(MongoCursor<Document> cursor) {
        ArrayList<Entity> list = new ArrayList<Entity>();
        while (cursor.hasNext()) {
            Entity entity = toEntity(cursor.next());
            list.add(entity);
        }
        return list;
    }

    final protected Entity getOne(Bson query) {
        assert query != null : "mongodb query should be be null";

        Document object = getCollection().find(query).first();
        if (object == null)
            throw new ResourceNotFoundException("resource not found in db");

        return toEntity(object);
    }

    final protected long generateNextId(String name) {
        assert name != null : "next id collection name should not be null";
        assert !name.isEmpty() : "next id collection name should not be empty";

        return counter.getNextId(name);
    }

    final protected MongoCollection<Document> getCollection() {
        assert getCollectionName() != null : "getCollectionName should not return null";
        assert !getCollectionName().isEmpty() : "getCollectionName should not return empty";

        return data_source.getDatabase().getCollection(getCollectionName());
    }

    private void generateId(Entity entity) {
        assert getCollectionName() != null : "getCollectionName should not return null";
        assert !getCollectionName().isEmpty() : "getCollectionName should not return empty";

        entity.setId(generateNextId(getCollectionName()));
    }

    protected abstract Document toDocument(Entity entity);

    protected abstract Entity toEntity(Document object);

    protected abstract Bson getModifyQuery(Entity entity);

    protected abstract String getCollectionName();
}
