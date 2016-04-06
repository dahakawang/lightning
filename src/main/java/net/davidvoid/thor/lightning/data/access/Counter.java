package net.davidvoid.thor.lightning.data.access;

import net.davidvoid.thor.lightning.data.source.MongoDataSource;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;

/**
 * Created by david on 3/22/16.
 * {_id: XX, name: string, seq: long }
 * index 1: name, unique
 *
 */
@Component
public class Counter {
	@Autowired
	MongoDataSource data_source = null;

	public long getNextId(String col_name) {
		BasicDBObject query = new BasicDBObject("name", col_name);
		BasicDBObject update = new BasicDBObject("$inc", new BasicDBObject("seq", 1L));

		MongoCollection<Document> col = data_source.getDatabase().getCollection("counter");
		FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
		options.upsert(true).returnDocument(ReturnDocument.AFTER);
		Document returned = col.findOneAndUpdate(query, update, options);

		return (long) returned.get("seq");
	}
}
