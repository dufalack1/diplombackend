package by.grsu.eventlink.configuration.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.validation.constraints.NotBlank;

@Getter
@ConstructorBinding
@AllArgsConstructor
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {

    @NotBlank
    private String title;

    @NotBlank
    private String appVersion;

    @NotBlank
    private String description;

    @NotBlank
    private String developerName;

    @NotBlank
    private String developerEmail;

}
