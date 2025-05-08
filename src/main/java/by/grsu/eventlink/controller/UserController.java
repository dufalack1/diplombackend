package by.grsu.eventlink.controller;

import by.grsu.eventlink.configuration.properties.enums.StorageCase;
import by.grsu.eventlink.controller.documentation.UserControllerDoc;
import by.grsu.eventlink.dto.auth.AuthenticationResponseDto;
import by.grsu.eventlink.dto.common.GenericMessageDto;
import by.grsu.eventlink.dto.user.UserDto;
import by.grsu.eventlink.dto.user.UserRegistrationRequestDto;
import by.grsu.eventlink.entity.enums.RoleHelper;
import by.grsu.eventlink.service.MinioService;
import by.grsu.eventlink.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController implements UserControllerDoc {

    private final UserService userService;

    private final MinioService minioService;

    @Override
    @GetMapping("{id}")
    public UserDto getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @Override
    @DeleteMapping("{id}")
    @PreAuthorize("@accessManagerService.isAuthorisedAdmittedToUser(#id, authentication)")
    public GenericMessageDto delete(@PathVariable Long id) {
        return userService.delete(id);
    }

    @Override
    @PutMapping("{id}")
    @PreAuthorize("@accessManagerService.isAuthorisedAdmittedToUser(#id, authentication)")
    public UserDto update(@PathVariable Long id, @RequestBody UserDto userDto) {
        return userService.update(id, userDto);
    }

    @Override
    @PutMapping("a-panel/{userId}")
    @PreAuthorize("@accessManagerService.isAuthorisedAdmin(authentication)")
    public void grantRole(@PathVariable Long userId, @RequestParam("title") RoleHelper role,
                          Authentication authentication) {
        userService.grantRole(userId, role, authentication.getName());
    }

    @PatchMapping("a-panel/{userId}")
    @PreAuthorize("@accessManagerService.isAuthorisedAdmin(authentication)")
    public void removeRole(@PathVariable Long userId, @RequestParam("role")RoleHelper role, Authentication authentication) {
        userService.removeRole(userId, role, authentication.getName());
    }

    @PutMapping("{id}/avatar")
    @PreAuthorize("@accessManagerService.isAuthorisedAdmittedToUser(#id, authentication)")
    public GenericMessageDto uploadAvatar(@PathVariable Long id, @RequestParam("picture") MultipartFile picture) {
        return minioService.uploadObject(picture, id, StorageCase.USER);
    }

    @Override
    @GetMapping
    public List<UserDto> getUsersByDisplayedNameMatch(@RequestParam("q") String query) {
        return userService.getUsersNicknameMatch(query);
    }

    @Override
    @PostMapping
    public AuthenticationResponseDto registration(@RequestBody @Validated UserRegistrationRequestDto registrationRequestDto) {
        return userService.registration(registrationRequestDto);
    }

}

