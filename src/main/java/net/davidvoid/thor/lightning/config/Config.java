package net.davidvoid.thor.lightning.config;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * Created by david on 3/20/16.
 */
public class Config extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected WebApplicationContext createRootApplicationContext() {
        WebApplicationContext context = super.createRootApplicationContext();
        ((ConfigurableEnvironment)context.getEnvironment()).setActiveProfiles("dev-c");
        return context;
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{ RootConfig.class, SecurityConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{ "/"};
    }
}
