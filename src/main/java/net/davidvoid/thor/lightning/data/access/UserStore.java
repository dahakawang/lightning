package net.davidvoid.thor.lightning.data.access;

import net.davidvoid.thor.lightning.entity.Entity;
import net.davidvoid.thor.lightning.entity.User;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Component;

import static org.springframework.util.Assert.*;

import com.mongodb.BasicDBObject;

/**
 * Created by david on 3/21/16. {_id: XXXX, id: long, name: string, password:
 * String} index 1: id, unique index 2: name, unique
 */
@Component
public class UserStore extends AbstractStore {

    private static final String COLLECTION_NAME = "user";
    
    public User getUser() {
        return (User) getOne(new BasicDBObject());
    }

    public User getByName(String name) {
        notNull(name);
        hasLength(name);
        
        BasicDBObject query = new BasicDBObject("name", name);
        return (User) getOne(query);
    }

    public User getById(long id) {
        BasicDBObject query = new BasicDBObject("id", id);
        return (User) getOne(query);
    }

    @Override
    protected Document toDocument(Entity entity) {
        assert entity != null;
        
        User user = (User) entity;
        Document object = new Document("name", user.getName());
        if (user.has_valid_id())
            object.put("id", user.getId());
        object.put("password", user.getPassword());

        return object;
    }

    @Override
    protected Entity toEntity(Document object) {
        assert object != null;
        
        User user = new User();
        user.setId((long) object.get("id"));
        user.setName((String) object.get("name"));
        user.setPassword((String) object.get("password"));

        return user;
    }

    @Override
    protected Bson getModifyQuery(Entity entity) {
        assert entity != null;
        
        User user = (User) entity;
        return new BasicDBObject("id", user.getId());
    }

    @Override
    protected String getCollectionName() {
        return COLLECTION_NAME;
    }
}
