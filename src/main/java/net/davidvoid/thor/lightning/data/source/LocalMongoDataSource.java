package net.davidvoid.thor.lightning.data.source;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Created by david on 4/6/16.
 */
@Component
@Profile("dev-h")
public class LocalMongoDataSource implements MongoDataSource {

    volatile MongoClient mongo = null;
    @Override
    public MongoClient getClient() {
        if (mongo != null) return mongo;

        synchronized (this) {
            if (mongo != null) return mongo;
            mongo = createInstance();
            return mongo;
        }
    }

    private MongoClient createInstance() {
        return new MongoClient();
    }

    @Override
    public MongoDatabase getDatabase() {
        return getClient().getDatabase("test");
    }
}
