package by.grsu.eventlink.dto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Entity {

    NODE("Node"),

    USER("User");

    private final String val;

}
