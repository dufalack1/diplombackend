package by.grsu.eventlink.configuration.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Getter
@ConstructorBinding
@AllArgsConstructor
@ConfigurationProperties(prefix = "minio.server")
public class MinioServerConfigurationProperties {

    @NotBlank
    private final String endPoint;

    @NotBlank
    private final String accessKey;

    @NotBlank
    private final String secretKey;

    @NotBlank
    private final String signerOverride;

    @NotBlank
    private final String maximumFileSize;

    @PositiveOrZero
    private final Integer connectionTimeout;

    @PositiveOrZero
    private final Integer requestTimeout;


}
