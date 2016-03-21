package net.davidvoid.thor.lightning.data.source;

import com.mongodb.MongoClient;

public interface MongoDataSource {
	public MongoClient getClient();
}
