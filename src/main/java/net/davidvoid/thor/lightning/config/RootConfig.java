package net.davidvoid.thor.lightning.config;

import net.davidvoid.thor.lightning.data.access.AbstractStore;
import net.davidvoid.thor.lightning.data.source.MongoDataSource;
import net.davidvoid.thor.lightning.service.Auth;
import net.davidvoid.thor.lightning.service.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {AbstractStore.class, Auth.class, MongoDataSource.class, JwtAuthenticationFilter.class})
public class RootConfig {
}
