package by.grsu.eventlink.mapper.user;

import by.grsu.eventlink.dto.user.TruncatedUserDto;
import by.grsu.eventlink.dto.user.UserDto;
import by.grsu.eventlink.entity.User;
import by.grsu.eventlink.mapper.category.CategoryMapper;
import by.grsu.eventlink.mapper.role.RoleMapper;
import liquibase.repackaged.org.apache.commons.collections4.CollectionUtils;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class UserMapper {

    public static List<TruncatedUserDto> mapToTruncatedDto(List<User> users) {
        return CollectionUtils.emptyIfNull(users).stream()
                .map(UserMapper::mapToTruncatedDto)
                .collect(Collectors.toList());
    }

    public static TruncatedUserDto mapToTruncatedDto(User user) {
        return TruncatedUserDto.builder()
                .id(user.getId())
                .username(user.getDisplayedName())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }

    public static List<UserDto> mapToDto(List<User> userList) {
        return CollectionUtils.emptyIfNull(userList).stream()
                .map(UserMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public static UserDto mapToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .birthDate(user.getBirthDate())
                .avatarUrl(user.getAvatarUrl())
                .phoneNumber(user.getPhoneNumber())
                .displayedName(user.getDisplayedName())
                .roles(RoleMapper.mapToDto(user.getCredentials().getRoles()))
                .likedCategories(CategoryMapper.mapToDto(user.getLikedCategories()))
                .build();
    }

    public static User mapToDo(UserDto user) {
        return User.builder()
                .id(user.getId())
                .birthDate(user.getBirthDate())
                .avatarUrl(user.getAvatarUrl())
                .phoneNumber(user.getPhoneNumber())
                .displayedName(user.getDisplayedName())
                .build();
    }

}
