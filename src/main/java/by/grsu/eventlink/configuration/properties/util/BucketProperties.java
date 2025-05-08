package by.grsu.eventlink.configuration.properties.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class BucketProperties {

    @NotBlank
    private String name;

    @NotBlank
    private String serverSideDefaultObjectPath;

}
