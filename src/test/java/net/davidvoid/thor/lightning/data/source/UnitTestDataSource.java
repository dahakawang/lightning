package net.davidvoid.thor.lightning.data.source;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.fakemongo.Fongo;
import com.mongodb.DB;
import com.mongodb.MongoClient;

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
    public DB getDatabase() {
        return fongo.getDB("test");
    }
}
