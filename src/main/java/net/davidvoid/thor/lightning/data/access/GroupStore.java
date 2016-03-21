package net.davidvoid.thor.lightning.data.access;

import com.mongodb.client.MongoCollection;
import net.davidvoid.thor.lightning.data.source.MongoDataSource;
import net.davidvoid.thor.lightning.entity.Group;
import net.davidvoid.thor.lightning.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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

    private MongoCollection getCollection() {
        return data_source.getDatabase().getCollection("group");
    }
}
