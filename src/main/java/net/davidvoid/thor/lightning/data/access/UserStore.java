package net.davidvoid.thor.lightning.data.access;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import net.davidvoid.thor.lightning.data.source.MongoDataSource;
import net.davidvoid.thor.lightning.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Created by david on 3/21/16.
 * {_id: XXXX, id: long, name: string, password: String}
 */
@Component
public class UserStore {
    @Autowired
    private MongoDataSource data_source = null;

    public User getByName(String name) {
        DBCollection col = getCollection();
        BasicDBObject query = new BasicDBObject("name", name);
        DBObject object = col.findOne(query);

        return to_user(object);
    }

    public User getById(long id) {
        DBCollection col = getCollection();
        BasicDBObject query = new BasicDBObject("id", id);
        DBObject object = col.findOne(query);

        return to_user(object);
    }

    public User addUser(User user) {
        assert(!user.is_valid_id());
        DBCollection col = getCollection();
        DBObject object = to_db_object(user);

        return null;
    }

    public void updateUser(User user) {
        assert(user.is_valid_id());

        DBCollection col = getCollection();
        BasicDBObject query = new BasicDBObject("id", user.getId());
        DBObject object = to_db_object(user);
        col.update(query, object);
    }

    private DBCollection getCollection() {
        return data_source.getDatabase().getCollection("user");
    }

    private  DBObject to_db_object(User user) {
        BasicDBObject object = new BasicDBObject("name", user.getName());
        if (user.is_valid_id()) object.put("id", user.getId());
        object.put("password", user.getPassword());

        return object;
    }

    private User to_user(DBObject object) {
        User user = new User();
        user.setId((long)object.get("id"));
        user.setName((String) object.get("name"));
        user.setPassword((String)object.get("password"));

        return user;
    }
}
