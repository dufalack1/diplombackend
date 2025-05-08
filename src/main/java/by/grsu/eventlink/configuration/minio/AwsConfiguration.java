package by.grsu.eventlink.configuration.minio;

import by.grsu.eventlink.configuration.properties.MinioServerConfigurationProperties;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

@Configuration
@RequiredArgsConstructor
public class AwsConfiguration {

    private final MinioServerConfigurationProperties minioProperties;

    @Bean
    public AmazonS3 s3Client() {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setSignerOverride(minioProperties.getSignerOverride());
        clientConfiguration.setConnectionTimeout(minioProperties.getConnectionTimeout());
        clientConfiguration.setRequestTimeout(minioProperties.getRequestTimeout());

        return AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder
                                .EndpointConfiguration(minioProperties.getEndPoint(), Regions.DEFAULT_REGION.getName())
                )
                .withPathStyleAccessEnabled(true)
                .withClientConfiguration(clientConfiguration)
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(minioProperties.getAccessKey(), minioProperties.getSecretKey()))
                )
                .build();
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.parse(minioProperties.getMaximumFileSize()));
        factory.setMaxRequestSize(DataSize.parse(minioProperties.getMaximumFileSize()));
        return factory.createMultipartConfig();
    }


}
