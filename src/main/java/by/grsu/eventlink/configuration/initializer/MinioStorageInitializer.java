package by.grsu.eventlink.configuration.initializer;

import by.grsu.eventlink.configuration.properties.MinioDataStorageProperties;
import by.grsu.eventlink.configuration.properties.enums.StorageCase;
import by.grsu.eventlink.configuration.properties.util.BucketProperties;
import com.amazonaws.auth.policy.Policy;
import com.amazonaws.auth.policy.Principal;
import com.amazonaws.auth.policy.Resource;
import com.amazonaws.auth.policy.Statement;
import com.amazonaws.auth.policy.actions.S3Actions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.InputStream;
import java.util.EnumSet;

@Slf4j
@Component
@RequiredArgsConstructor
public class MinioStorageInitializer {

    private final AmazonS3 amazonS3Client;

    private final ResourceLoader resourceLoader;

    private final MinioDataStorageProperties minioProperties;

    @PostConstruct
    public void storageInitialize() {
        EnumSet.allOf(StorageCase.class)
                .forEach(storageCase -> this.setupAndPutDefaultObject(minioProperties.getBuckets().get(storageCase)));
    }

    private void setupAndPutDefaultObject(BucketProperties bucketProperties) {
        if (!amazonS3Client.doesBucketExistV2(bucketProperties.getName())) {
            amazonS3Client.createBucket(bucketProperties.getName());
            amazonS3Client.setBucketPolicy(bucketProperties.getName(),
                    this.getPublicReadPolicy(bucketProperties.getName()));
            amazonS3Client.putObject(
                    bucketProperties.getName(),
                    minioProperties.getDefaultImageName(),
                    getServerSideObject(bucketProperties.getServerSideDefaultObjectPath()),
                    null
            );
            log.info(String.format("Bucket %s was not exist, I create it.", bucketProperties.getName()));
        }
    }

    private String getPublicReadPolicy(String bucketName) {
        Policy bucketPolicy = new Policy().withStatements(
                new Statement(Statement.Effect.Allow)
                        .withPrincipals(Principal.AllUsers)
                        .withActions(S3Actions.GetObject)
                        .withResources(new Resource(
                                String.format("arn:aws:s3:::%s/*", bucketName))));
        return bucketPolicy.toJson();
    }

    @SneakyThrows
    private InputStream getServerSideObject(String serverSidePath) {
        return resourceLoader.getResource(String.format("classpath:%s", serverSidePath)).getInputStream();
    }


}
