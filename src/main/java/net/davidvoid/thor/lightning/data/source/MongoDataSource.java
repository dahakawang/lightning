package net.davidvoid.thor.lightning.data.source;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public interface MongoDataSource {
	MongoClient getClient();
	MongoDatabase getDatabase();
}
