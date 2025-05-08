package by.grsu.eventlink.mapper.role;

import by.grsu.eventlink.entity.Role;
import by.grsu.eventlink.entity.enums.RoleHelper;
import liquibase.repackaged.org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class RoleMapper {

    public static List<RoleHelper> mapToDto(List<Role> roles) {
        return CollectionUtils.emptyIfNull(roles).stream()
                .map(role -> RoleHelper.lookup(role.getTitle()))
                .collect(Collectors.toList());
    }

}
