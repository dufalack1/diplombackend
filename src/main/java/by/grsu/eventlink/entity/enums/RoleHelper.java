package by.grsu.eventlink.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum RoleHelper {

    USER("USER"), ADMIN("ADMIN"), BLOCKED("BLOCKED"), MODERATOR("MODERATOR");

    private static final Map<String, RoleHelper> LOOKUP;

    static {
        LOOKUP = Collections.unmodifiableMap(Arrays.stream(RoleHelper.values())
                .collect(Collectors.toMap(RoleHelper::getTitle, Function.identity())));
    }

    private final String title;

    public static RoleHelper lookup(String val) {
        return Optional.ofNullable(LOOKUP.get(val)).orElseThrow(() -> new IllegalArgumentException("Unknown value " + val));
    }

}
