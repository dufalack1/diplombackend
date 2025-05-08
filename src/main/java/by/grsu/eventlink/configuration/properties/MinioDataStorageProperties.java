package by.grsu.eventlink.configuration.properties;

import by.grsu.eventlink.configuration.properties.enums.StorageCase;
import by.grsu.eventlink.configuration.properties.util.BucketProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Map;

@Getter
@ConstructorBinding
@AllArgsConstructor
@ConfigurationProperties(prefix = "minio.storage")
public class MinioDataStorageProperties {

    @NotBlank
    private final String defaultImageName;

    @NotBlank
    private final String redirectionEndPoint;

    @NotEmpty
    private final String[] allowedPhotoFormats;

    @NotEmpty
    private Map<StorageCase, BucketProperties> buckets;

}
