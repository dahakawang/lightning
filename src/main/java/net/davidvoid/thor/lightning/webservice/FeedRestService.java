package net.davidvoid.thor.lightning.webservice;

import net.davidvoid.thor.lightning.entity.Feed;
import net.davidvoid.thor.lightning.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static net.davidvoid.thor.lightning.util.HttpRequestAssertion.assertStringNotEmpty;
import static net.davidvoid.thor.lightning.util.HttpRequestAssertion.isTrue;

/**
 * Created by david on 4/30/16.
 */
@RestController
@RequestMapping("/groups/{group_id}/feeds")
public class FeedRestService {
    @Autowired
    FeedService service = null;

    @RequestMapping(method = RequestMethod.GET)
    public List<Feed> get(@PathVariable Long group_id) {
        return service.getFeeds(group_id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Feed add(@PathVariable Long group_id, @RequestBody Map<String, Object> data) {
        Feed feed = parseNewObject(data);
        return service.add(group_id, feed);
    }

    @RequestMapping(value = "/{feed_id}", method = RequestMethod.PUT)
    public Feed update(@PathVariable Long group_id, @PathVariable Long feed_id, @RequestBody Map<String, Object> data) {
        Feed feed = parseUpdateObject(data);
        feed.setId(feed_id);

        return service.update(group_id, feed);
    }

    @RequestMapping(value = "/{feed_id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long group_id, @PathVariable Long feed_id) {
        service.delete(group_id, feed_id);
    }

    private Feed parseNewObject(Map<String, Object> data) {
        isTrue(data.size() == 3);
        assertStringNotEmpty(data.get("name"), "name field not present");
        assertStringNotEmpty(data.get("description"), "description field not present");
        assertStringNotEmpty(data.get("url"), "url field not present");

        Feed feed = new Feed();
        feed.setDescription((String) data.get("name"));
        feed.setName((String) data.get("description"));
        feed.setUrl((String) data.get("url"));
        return feed;
    }

    private Feed parseUpdateObject(Map<String, Object> data) {
        isTrue(data.size() == 2);
        assertStringNotEmpty(data.get("name"), "name field not present");
        assertStringNotEmpty(data.get("description"), "description field not present");

        Feed feed = new Feed();
        feed.setDescription((String) data.get("name"));
        feed.setName((String) data.get("description"));
        return feed;
    }
}
