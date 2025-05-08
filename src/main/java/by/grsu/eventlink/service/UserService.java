package by.grsu.eventlink.service;

import by.grsu.eventlink.dto.auth.AuthenticationResponseDto;
import by.grsu.eventlink.dto.common.GenericMessageDto;
import by.grsu.eventlink.dto.user.UserDto;
import by.grsu.eventlink.dto.user.UserRegistrationRequestDto;
import by.grsu.eventlink.entity.enums.RoleHelper;

import java.util.List;

public interface UserService {

    UserDto getById(Long id);

    GenericMessageDto delete(Long userId);

    UserDto update(Long id, UserDto userDto);

    List<UserDto> getUsersNicknameMatch(String query);

    void grantRole(Long userId, RoleHelper role, String email);

    void removeRole(Long userId, RoleHelper role, String email);

    AuthenticationResponseDto registration(UserRegistrationRequestDto userRegistrationRequest);

}
