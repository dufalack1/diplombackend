package by.grsu.eventlink.configuration;

import by.grsu.eventlink.configuration.properties.MinioDataStorageProperties;
import by.grsu.eventlink.configuration.properties.MinioServerConfigurationProperties;
import by.grsu.eventlink.configuration.properties.SecurityProperties;
import by.grsu.eventlink.configuration.properties.SwaggerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableRetry
@EnableAsync
@Configuration
@EnableConfigurationProperties({
        SwaggerProperties.class,
        SecurityProperties.class,
        MinioDataStorageProperties.class,
        MinioServerConfigurationProperties.class
})
public class CommonConfiguration {



}
