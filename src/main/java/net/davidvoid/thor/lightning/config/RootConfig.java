package net.davidvoid.thor.lightning.config;

import net.davidvoid.thor.lightning.data.access.AbstractStore;
import net.davidvoid.thor.lightning.data.source.DataSourceConfig;
import net.davidvoid.thor.lightning.service.Auth;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(DataSourceConfig.class)
@ComponentScan(basePackageClasses = {AbstractStore.class, Auth.class})
public class RootConfig {
}
