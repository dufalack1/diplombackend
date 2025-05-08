package by.grsu.eventlink.service;

import by.grsu.eventlink.dto.auth.AuthenticationRequestDto;
import by.grsu.eventlink.dto.auth.AuthenticationResponseDto;

public interface AuthenticationService {

    AuthenticationResponseDto authenticate(AuthenticationRequestDto authenticationRequest);

}
