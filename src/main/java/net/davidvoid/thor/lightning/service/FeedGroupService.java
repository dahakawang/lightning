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
        User user = userStore.getById(SessionUser.get().getId());
        return store.getGroups(user);
    }
    
    public void delete(long group_id) {
        
    }
    
}
