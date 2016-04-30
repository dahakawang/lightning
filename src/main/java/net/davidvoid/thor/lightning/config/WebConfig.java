package net.davidvoid.thor.lightning.config;

import net.davidvoid.thor.lightning.webservice.UserRestService;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackageClasses = {UserRestService.class})
public class WebConfig {
}
