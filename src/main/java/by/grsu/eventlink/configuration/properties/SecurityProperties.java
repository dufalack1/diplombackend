package by.grsu.eventlink.configuration.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.validation.constraints.NotBlank;

@Getter
@ConstructorBinding
@AllArgsConstructor
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    @NotBlank
    private String secretWord;

    @NotBlank
    private Long tokenExpiration;

}
