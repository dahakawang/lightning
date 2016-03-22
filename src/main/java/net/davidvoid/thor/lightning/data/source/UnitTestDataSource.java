package net.davidvoid.thor.lightning.data.source;

import com.github.fakemongo.Fongo;
import com.mongodb.DB;
import com.mongodb.MongoClient;

/**
 * Created by david on 3/22/16.
 */
public class UnitTestDataSource implements MongoDataSource {
    @Override
    public MongoClient getClient() {
        return null;
    }

    @Override
    public DB getDatabase() {
        Fongo fongo = new Fongo("test");
        return fongo.getDB("test");
    }
}
