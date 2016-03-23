package net.davidvoid.thor.lightning.data.source;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public interface MongoDataSource {
	MongoClient getClient();
	DB getDatabase();
}
