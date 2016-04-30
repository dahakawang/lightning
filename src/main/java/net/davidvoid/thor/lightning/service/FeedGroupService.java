package net.davidvoid.thor.lightning.service;

import java.util.List;

import net.davidvoid.thor.lightning.data.access.GroupStore;
import net.davidvoid.thor.lightning.data.access.UserStore;
import net.davidvoid.thor.lightning.entity.Group;
import net.davidvoid.thor.lightning.entity.User;
import net.davidvoid.thor.lightning.util.SessionUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class FeedGroupService {
    @Autowired
    GroupStore store = null;
    @Autowired
    UserStore userStore = null;
    
    public List<Group> getAllGroups() {
        return store.getGroups(userInSession());
    }

    public Group add(Group group) {
        group.setUser(userInSession());
        store.add(group);
        return group;
    }
    
    public Group update(Group group) {
        group.setUser(userInSession());
        store.update(group);
        return group;
    }

    public void delete(Object group_id) {
        store.deleteById(group_id);
    }

    private User userInSession() {
        Object uid = SessionUser.get().getId();
        return userStore.getById(uid);
    }

}
