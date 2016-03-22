package net.davidvoid.thor.lightning.data.source;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class DataSourceConfig {

    @Bean
    @Profile("test")
    public MongoDataSource createUnitTest() {
        return new UnitTestDataSource();
    }

}
