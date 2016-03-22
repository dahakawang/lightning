package net.davidvoid.thor.lightning.data.access;

import java.util.List;

import net.davidvoid.thor.lightning.data.source.MongoDataSource;
import net.davidvoid.thor.lightning.entity.Group;
import net.davidvoid.thor.lightning.entity.User;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.DBCollection;

/**
 * Created by david on 3/21/16.
 */
public class GroupStore {
    @Autowired
    private MongoDataSource data_source = null;

    public List<Group> getGroups(User user) {
        return null;
    }

    public Group addGroup(Group group) {
        return null;
    }

    public void updateGroup(Group group) {

    }

    private DBCollection getCollection() {
        return data_source.getDatabase().getCollection("group");
    }
}
