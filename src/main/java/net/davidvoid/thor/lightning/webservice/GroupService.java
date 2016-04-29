package net.davidvoid.thor.lightning.webservice;

import java.util.List;

import net.davidvoid.thor.lightning.entity.Group;
import net.davidvoid.thor.lightning.service.FeedGroupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/groups")
public class GroupService {
    @Autowired
    FeedGroupService service = null;
    
    @RequestMapping(method = RequestMethod.GET)
    public List<Group> get() {
        return service.getAllGroups();
    }
    
    @RequestMapping(value = "/{group_id}", method = RequestMethod.DELETE)
    public void delete() {
        
    }
    
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Group add() {
        return null;
    }
    
    
    @RequestMapping(value = "/{group_id}", method = RequestMethod.PUT)
    public Group update() {
        return null;
    }
}
