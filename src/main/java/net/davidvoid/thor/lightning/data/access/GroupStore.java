package net.davidvoid.thor.lightning.data.access;

import java.util.List;

import net.davidvoid.thor.lightning.entity.Entity;
import net.davidvoid.thor.lightning.entity.Group;
import net.davidvoid.thor.lightning.entity.User;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Component;

import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

import com.mongodb.BasicDBObject;

/**
 * Created by david on 3/21/16. {_id: XX, user_id: long, id: long, name: string}
 * index 1: user_id,id, unique
 */
@Component
public class GroupStore extends AbstractStore {

    private static final String COLLECTION_NAME = "group";

    @SuppressWarnings("unchecked")
    public List<Group> getGroups(User user) {
        notNull(user);
        isTrue(user.has_valid_id(), "invalid user given to find the groups");

        BasicDBObject query = new BasicDBObject("user_id", user.getId());
        return inject_user((List<Group>) (List<?>) get(query), user);
    }

    public Group getGroupById(Object id) {
        notNull(id);
        isTrue(Entity.is_valid_id(id), "invalid group ID given");

        BasicDBObject query =  new BasicDBObject("id", id);
        return (Group) getOne(query);
    }

    private List<Group> inject_user(List<Group> list, User user) {
        assert user.has_valid_id() : "user is invalid";
        assert list != null;

        for (Group group : list) {
            group.setUser(user);
        }
        return list;
    }

    @Override
    protected Document toDocument(Entity entity) {
        assert entity != null;
        
        Group group = (Group) entity;
        User user = group.getUser();
        notNull(user, "a valid group object should tied to a valid user object");
        isTrue(user.has_valid_id(), "a valid group object should tied to a valid user object");

        Document object = new Document("user_id", user.getId());
        object.put("id", group.getId());
        object.put("name", group.getName());
        return object;
    }

    @Override
    protected Entity toEntity(Document object) {
        assert object != null;
        
        Group group = new Group();
        group.setId((long) object.get("id"));
        group.setName((String) object.get("name"));

        return group;
    }

    @Override
    protected Bson getModifyQuery(Entity entity) {
        assert entity != null;
        
        Group group = (Group) entity;
        User user = group.getUser();
        notNull(user, "a valid group object should tied to a valid user object");
        isTrue(user.has_valid_id(), "a valid group object should tied to a valid user object");

        BasicDBObject query = new BasicDBObject("user_id", user.getId());
        query.put("id", group.getId());
        return query;
    }

    @Override
    protected String getCollectionName() {
        return COLLECTION_NAME;
    }
}
