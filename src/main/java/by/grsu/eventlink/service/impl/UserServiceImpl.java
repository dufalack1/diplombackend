package by.grsu.eventlink.service.impl;

import by.grsu.eventlink.configuration.properties.enums.StorageCase;
import by.grsu.eventlink.dto.auth.AuthenticationRequestDto;
import by.grsu.eventlink.dto.auth.AuthenticationResponseDto;
import by.grsu.eventlink.dto.common.GenericMessageDto;
import by.grsu.eventlink.dto.enums.Entity;
import by.grsu.eventlink.dto.user.UserDto;
import by.grsu.eventlink.dto.user.UserRegistrationRequestDto;
import by.grsu.eventlink.entity.Credential;
import by.grsu.eventlink.entity.Invitation;
import by.grsu.eventlink.entity.Role;
import by.grsu.eventlink.entity.User;
import by.grsu.eventlink.entity.enums.RoleHelper;
import by.grsu.eventlink.exception.common.InternalServerError;
import by.grsu.eventlink.exception.invitation.InvitationCodeMissMatchException;
import by.grsu.eventlink.exception.invitation.InvitationNotFoundException;
import by.grsu.eventlink.exception.role.RoleNotFoundException;
import by.grsu.eventlink.exception.role.UserAlreadyHaveFollowingRoleException;
import by.grsu.eventlink.exception.role.UserDontHaveFollowingRoleException;
import by.grsu.eventlink.exception.user.UserAlreadyExistException;
import by.grsu.eventlink.exception.user.UserNotFoundException;
import by.grsu.eventlink.mail.Postman;
import by.grsu.eventlink.mapper.user.UserMapper;
import by.grsu.eventlink.repository.CredentialRepository;
import by.grsu.eventlink.repository.InvitationRepository;
import by.grsu.eventlink.repository.RoleRepository;
import by.grsu.eventlink.repository.UserRepository;
import by.grsu.eventlink.service.AuthenticationService;
import by.grsu.eventlink.service.UserService;
import by.grsu.eventlink.service.util.StorageHelper;
import by.grsu.eventlink.util.collection.Collections;
import by.grsu.eventlink.util.date.DateUtils;
import by.grsu.eventlink.util.helper.Conditional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Postman greetingsPostman;

    private final StorageHelper storageHelper;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final CredentialRepository credentialRepository;

    private final InvitationRepository invitationRepository;

    private final AuthenticationService authenticationService;

    @Override
    @Transactional
    public AuthenticationResponseDto registration(UserRegistrationRequestDto userRegistrationRequest) {
        Invitation candidateInvitation = invitationRepository.getInvitationByEmailAndExpirationTimeAfter(
                userRegistrationRequest.getEmail(), DateUtils.getDateNow())
                .orElseThrow(InvitationNotFoundException::new);

        Conditional.fromBoolean(candidateInvitation.getGeneratedCode()
                .equals(userRegistrationRequest.getConfirmationCode()))
                        .ifFalseThenThrow(InvitationCodeMissMatchException::new);

        Conditional.fromBoolean(userRepository.existsByEmailOrDisplayedNameOrPhoneNumber(
                        userRegistrationRequest.getEmail(),
                        userRegistrationRequest.getUsername(),
                        userRegistrationRequest.getPhoneNumber()))
                .ifTrueThenThrow(UserAlreadyExistException::new);

        invitationRepository.delete(candidateInvitation);

        Role userRole = roleRepository.getRoleByTitle(RoleHelper.USER.getTitle())
                .orElseThrow(InternalServerError::new);

        User candidate = User.builder()
                .avatarUrl(storageHelper.getDefaultAvatarName())
                .birthDate(userRegistrationRequest.getBirthDate())
                .displayedName(userRegistrationRequest.getUsername())
                .phoneNumber(userRegistrationRequest.getPhoneNumber())
                .build();

        Credential candidateCredentials = Credential.builder()
                .email(userRegistrationRequest.getEmail())
                .roles(java.util.Collections.singletonList(userRole))
                .password(passwordEncoder.encode(userRegistrationRequest.getPassword()))
                .user(candidate)
                .build();
        userRole.getCredentials().add(candidateCredentials);
        candidate.setCredentials(candidateCredentials);

        userRepository.save(candidate);
        credentialRepository.save(candidateCredentials);

        greetingsPostman.send(userRegistrationRequest.getEmail(),
                java.util.Collections.singletonList(userRegistrationRequest.getUsername()));

        return authenticationService.authenticate(AuthenticationRequestDto.builder()
                .email(userRegistrationRequest.getEmail())
                .password(userRegistrationRequest.getPassword())
                .build());
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {
        User currentUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (Objects.nonNull(userDto.getDisplayedName()) &&
                !userDto.getDisplayedName().equalsIgnoreCase(currentUser.getDisplayedName())) {
            Conditional.fromBoolean(userRepository.existsByDisplayedNameIgnoreCase(userDto.getDisplayedName()))
                    .ifTrueThenThrow(UserAlreadyExistException::new);
        }

        currentUser.setBirthDate(Optional.ofNullable(userDto.getBirthDate())
                .orElse(currentUser.getBirthDate()));
        currentUser.setPhoneNumber(Optional.ofNullable(userDto.getPhoneNumber())
                .orElse(currentUser.getPhoneNumber()));
        currentUser.setDisplayedName(Optional.ofNullable(userDto.getDisplayedName())
                .orElse(currentUser.getDisplayedName()));

        userRepository.save(currentUser);

        currentUser.setAvatarUrl(storageHelper.constructUrl(currentUser.getAvatarUrl(), StorageCase.USER));
        return UserMapper.mapToDto(currentUser);
    }


    @Override
    public List<UserDto> getUsersNicknameMatch(String query) {
        List<User> foundedUsers = userRepository.findUsersByDisplayedNameContainingIgnoreCase(query,
                Pageable.ofSize(10));

        Collections.ifEmptyThenThrow(foundedUsers);

        foundedUsers.forEach(
                currentUser -> currentUser.setAvatarUrl(storageHelper.constructUrl(currentUser.getAvatarUrl(),
                        StorageCase.USER)));
        return foundedUsers.stream()
                .map(UserMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void grantRole(Long userId, RoleHelper role, String email) {
        User candidate = userRepository.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Conditional.fromBoolean(candidate.getCredentials().getEmail().equals(email))
                .ifTrueThenThrow(UserAlreadyHaveFollowingRoleException::new);

        Role roleToGrant = roleRepository.getRoleByTitle(role.getTitle())
                .orElseThrow(() -> new RoleNotFoundException(role.getTitle()));

        Conditional.fromBoolean(candidate.getCredentials().getRoles().stream()
                        .anyMatch(r -> r.getTitle().equals(role.getTitle())))
                .ifTrueThenThrow(() -> new UserAlreadyHaveFollowingRoleException(userId));

        Credential candidateCredentials = candidate.getCredentials();
        candidateCredentials.getRoles().add(roleToGrant);
        credentialRepository.save(candidateCredentials);
        roleToGrant.getCredentials().add(candidateCredentials);
        roleRepository.save(roleToGrant);
    }

    @Override
    @Transactional
    public void removeRole(Long userId, RoleHelper role, String email) {
        User candidate = userRepository.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Conditional.fromBoolean(candidate.getCredentials().getEmail().equals(email))
                .ifTrueThenThrow(UserDontHaveFollowingRoleException::new);

        Role roleToGrant = roleRepository.getRoleByTitle(role.getTitle())
                .orElseThrow(() -> new RoleNotFoundException(role.getTitle()));

        Conditional.fromBoolean(candidate.getCredentials().getRoles().stream()
                        .noneMatch(r -> r.getTitle().equals(role.getTitle())))
                .ifTrueThenThrow(() -> new UserDontHaveFollowingRoleException(userId));

        Credential candidateCredentials = candidate.getCredentials();
        candidateCredentials.getRoles().remove(roleToGrant);
        credentialRepository.save(candidateCredentials);
        roleToGrant.getCredentials().remove(candidateCredentials);
        roleRepository.save(roleToGrant);
    }

    @Override
    public UserDto getById(Long id) {
        User currentUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        currentUser.setAvatarUrl(storageHelper.constructUrl(currentUser.getAvatarUrl(), StorageCase.USER));
        return UserMapper.mapToDto(currentUser);
    }

    @Override
    public GenericMessageDto delete(Long userId) {
        User deactivationCandidate = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        userRepository.delete(deactivationCandidate);

        return GenericMessageDto.builder()
                .entityType(Entity.USER)
                .entityId(deactivationCandidate.getId())
                .message("User with provided ID was deleted successfully")
                .build();
    }

}
