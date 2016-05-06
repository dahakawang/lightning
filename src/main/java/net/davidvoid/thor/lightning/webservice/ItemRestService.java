package net.davidvoid.thor.lightning.webservice;

import java.util.List;

import net.davidvoid.thor.lightning.data.access.ItemStore.SORT_OPTION;
import net.davidvoid.thor.lightning.entity.Item;
import net.davidvoid.thor.lightning.service.ItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/groups/{group_id}/feeds/{feed_id}/items")
public class ItemRestService {
    @Autowired
    ItemService service = null;
    
    @RequestMapping(method = RequestMethod.GET)
    public List<Item> get(@PathVariable Long group_id, @PathVariable Long feed_id, 
            @RequestParam(required = false, defaultValue = "false") boolean onlyUnread, 
            @RequestParam(required = false, defaultValue = "BY_UPDATE_DATE_DSC") SORT_OPTION sortBy,
            @RequestParam(required = false, defaultValue = "0") int start, 
            @RequestParam(required = false, defaultValue = "10") int count) {
        return service.get(group_id, feed_id, onlyUnread, sortBy, start, count);
    }
    
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public long count(@PathVariable Long group_id, @PathVariable Long feed_id, 
            @RequestParam(required = false, defaultValue = "false") boolean onlyUnread) {
        return service.count(group_id, feed_id, onlyUnread);
    }

}
