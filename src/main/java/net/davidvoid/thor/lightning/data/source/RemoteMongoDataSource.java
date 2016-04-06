package net.davidvoid.thor.lightning.data.source;

import java.util.Arrays;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

@Component
@Profile("dev-c")
public class RemoteMongoDataSource implements MongoDataSource {
    
    volatile MongoClient mongo = null;

    @Override
    public MongoClient getClient() {
        if (mongo != null) return mongo;
        
        synchronized(this) {
            if (mongo != null) return mongo;
            mongo = createInstance();
            return mongo;
        }
    }

    private MongoClient createInstance() {
        MongoCredential credential = MongoCredential.createCredential("admin", "admin", "admin1".toCharArray());
        return new MongoClient(new ServerAddress("vm-4538-9e7c.nam.nsroot.net:32017"), Arrays.asList(credential));
    }

    @Override
    public MongoDatabase getDatabase() {
        return getClient().getDatabase("test");
    }

}
