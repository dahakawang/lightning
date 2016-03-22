package net.davidvoid.thor.lightning.data.access;

import java.util.List;

import net.davidvoid.thor.lightning.entity.Entity;
import net.davidvoid.thor.lightning.entity.Group;
import net.davidvoid.thor.lightning.entity.User;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Created by david on 3/21/16. {_id: XX, user_id: long, id: long, name: string}
 * index 1: user_id,id, unique
 */
public class GroupStore extends AbstractStore {

    private static final String COLLECTION_NAME = "group";

    @SuppressWarnings("unchecked")
    public List<Group> getGroups(User user) {
        assert (user.has_valid_id());

        BasicDBObject query = new BasicDBObject("user_id", user.getId());
        return (List<Group>) (List<?>) get(query);
    }

    @Override
    protected DBObject toDBObject(Entity entity) {
        Group group = (Group) entity;
        User user = group.getUser();
        assert (user != null);

        BasicDBObject object = new BasicDBObject("user_id", user.getId());
        object.put("id", group.getId());
        object.put("name", group.getName());
        return object;
    }

    @Override
    protected Entity toEntity(DBObject object) {
        Group group = new Group();
        group.setId((long) object.get("id"));
        group.setName((String) object.get("name"));

        return group;
    }

    @Override
    protected DBObject getModifyQuery(Entity entity) {
        Group group = (Group) entity;
        User user = group.getUser();
        assert (user != null);

        DBObject query = new BasicDBObject("user_id", user.getId());
        query.put("id", group.getId());
        return query;
    }

    @Override
    protected String getCollectionName() {
        return COLLECTION_NAME;
    }
}
