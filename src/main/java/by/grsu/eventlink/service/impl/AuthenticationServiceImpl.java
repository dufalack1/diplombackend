package by.grsu.eventlink.service.impl;

import by.grsu.eventlink.configuration.properties.enums.StorageCase;
import by.grsu.eventlink.dto.auth.AuthenticationRequestDto;
import by.grsu.eventlink.dto.auth.AuthenticationResponseDto;
import by.grsu.eventlink.entity.Credential;
import by.grsu.eventlink.entity.Role;
import by.grsu.eventlink.entity.User;
import by.grsu.eventlink.repository.CredentialRepository;
import by.grsu.eventlink.repository.UserRepository;
import by.grsu.eventlink.security.jwt.exception.AuthenticationFailed;
import by.grsu.eventlink.security.jwt.token.JwtTokenProvider;
import by.grsu.eventlink.service.AuthenticationService;
import by.grsu.eventlink.service.util.StorageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final StorageHelper storageHelper;

    private final UserRepository userRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final CredentialRepository credentialRepository;

    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponseDto authenticate(AuthenticationRequestDto authenticationRequest) {
        Credential authenticationTarget = credentialRepository.getCredentialByEmail(authenticationRequest.getEmail())
                .orElseThrow(AuthenticationFailed::new);

        User currentUser = userRepository.findByCredentials_Email(authenticationRequest.getEmail())
                        .orElseThrow(AuthenticationFailed::new);

        authenticationManager.authenticate(extractAuthTokenObject(authenticationRequest));

        return AuthenticationResponseDto.builder()
                .id(currentUser.getId())
                .username(authenticationTarget.getUser().getDisplayedName())
                .avatarUrl(storageHelper.constructUrl(currentUser.getAvatarUrl(), StorageCase.USER))
                .token(jwtTokenProvider.createToken(authenticationRequest.getEmail(),
                        authenticationTarget.getRoles().stream().toList()))
                .roles(currentUser.getCredentials().getRoles().stream()
                        .map(Role::getTitle)
                        .collect(Collectors.toList()))
                .build();
    }

    private UsernamePasswordAuthenticationToken extractAuthTokenObject(AuthenticationRequestDto authenticationRequest) {
        return new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                authenticationRequest.getPassword());
    }

}
