package net.davidvoid.thor.lightning.data.access;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.inc;
import net.davidvoid.thor.lightning.data.source.MongoDataSource;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
		MongoCollection<Document> col = data_source.getDatabase().getCollection("counter");
		Bson query = eq("name", col_name);
		Bson update = inc("seq", 1L);

		FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
		options.upsert(true).returnDocument(ReturnDocument.AFTER);
		Document returned = col.findOneAndUpdate(query, update, options);

		return (long) returned.get("seq");
	}
}
