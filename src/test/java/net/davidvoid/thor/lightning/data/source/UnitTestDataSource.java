package net.davidvoid.thor.lightning.data.source;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.fakemongo.Fongo;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 * Created by david on 3/22/16.
 */
@Component
@Profile("test")
public class UnitTestDataSource implements MongoDataSource {

    Fongo fongo = new Fongo("unit_test");

    @Override
    public MongoClient getClient() {
        return null;
    }

    @Override
    public MongoDatabase getDatabase() {
        return fongo.getDatabase("test");
    }
}
