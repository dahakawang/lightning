package net.davidvoid.thor.lightning.webservice;

import java.util.List;
import java.util.Map;

import net.davidvoid.thor.lightning.entity.Group;
import net.davidvoid.thor.lightning.service.FeedGroupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static net.davidvoid.thor.lightning.util.HttpRequestAssertion.assertStringNotEmpty;
import static net.davidvoid.thor.lightning.util.HttpRequestAssertion.isTrue;

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
    public void delete(@PathVariable Long group_id) {
        service.delete(group_id);
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public Group add(@RequestBody Map<String, Object> data) {
        isTrue(data.size() == 1);
        assertStringNotEmpty(data.get("name"));

        Group group = new Group();
        group.setName((String) data.get("name"));
        return service.add(group);
    }
    
    
    @RequestMapping(value = "/{group_id}", method = RequestMethod.PUT)
    public Group update(@PathVariable Long group_id, @RequestBody Map<String, Object> data) {
        isTrue(data.size() == 1);
        assertStringNotEmpty(data.get("name"));

        Group group = new Group();
        group.setName((String) data.get("name"));
        group.setId(group_id);
        return service.update(group);
    }
}
