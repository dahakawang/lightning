package net.davidvoid.thor.lightning.data.access;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.davidvoid.thor.lightning.data.source.MongoDataSource;
import net.davidvoid.thor.lightning.entity.Group;
import net.davidvoid.thor.lightning.entity.User;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * Created by david on 3/21/16.
 * {_id: XX, user_id: long, id: long, name: string, feeds: [long]}
 * index 1: user_id,id, unique
 */
public class GroupStore {
    @Autowired
    private MongoDataSource data_source = null;
    @Autowired
    private Counter counter = null;

    public List<Group> getGroups(User user) {
    	assert(user.has_valid_id());
    	
    	DBCollection col = getCollection();
    	BasicDBObject query = new BasicDBObject("user_id", user.getId());
    	DBCursor cursor = col.find(query);
    	
    	return getAllGroups(cursor);
    }

	public Group addGroup(User user, Group group) {
		assert(user.has_valid_id());
		assert(!group.has_valid_id());
		
		long id = nextId();
		group.setId(id);
		DBObject object = to_object(user, group);
		
		DBCollection col = getCollection();
		col.insert(object);

        return group;
    }

    public void updateGroup(User user, Group group) {
    	assert(group.has_valid_id());
    	DBCollection col = getCollection();
    	DBObject query = get_update_query(user, group);
    	DBObject object = to_object(user, group);
    	
    	col.update(query, object);
    }

	private DBObject get_update_query(User user, Group group) {
		DBObject query = new BasicDBObject("user_id", user.getId());
		query.put("id", group.getId());
		return query;
	}

	private DBObject to_object(User user, Group group) {
		BasicDBObject object = new BasicDBObject("id", group.getId());
		object.put("name", group.getName());
		BasicDBList feeds_list = to_mongo_list(group.getFeeds());
		object.put("feeds", feeds_list);
		
		return object;
	}

	private BasicDBList to_mongo_list(List<Long> feeds) {
		BasicDBList list = new BasicDBList();
		
		Iterator<Long> it = feeds.iterator();
		while(it.hasNext()) {
			list.add(it.next());
		}
		return list;
	}
	
	private List<Long> to_array_list(BasicDBList list) {
		ArrayList<Long> ret = new ArrayList<Long>();
		
		Iterator<Object> it = list.iterator();
		while(it.hasNext()) {
			ret.add((Long)it.next());
		}
		
		return ret;
	}

	private long nextId() {
		return counter.getNextId("group");
	}

	private List<Group> getAllGroups(DBCursor cursor) {
		List<Group> list = new LinkedList<Group>();
    	while(cursor.hasNext()) {
    		DBObject object = cursor.next();
    		Group group = to_group(object);
    		list.add(group);
    	}
		return list;
	}

    private Group to_group(DBObject object) {
    	Group group = new Group();
    	group.setId((long)object.get("id"));
    	group.setName((String)object.get("name"));
    	group.setFeeds(to_array_list((BasicDBList)object.get("feeds")));
    	
    	return group;
	}

    private DBCollection getCollection() {
        return data_source.getDatabase().getCollection("group");
    }
}
