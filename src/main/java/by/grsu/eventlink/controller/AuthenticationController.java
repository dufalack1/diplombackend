package by.grsu.eventlink.controller;

import by.grsu.eventlink.controller.documentation.AuthenticationControllerDoc;
import by.grsu.eventlink.dto.auth.AuthenticationRequestDto;
import by.grsu.eventlink.dto.auth.AuthenticationResponseDto;
import by.grsu.eventlink.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class AuthenticationController implements AuthenticationControllerDoc {

    private final AuthenticationService authenticationService;

    @Override
    @PostMapping("auth")
    public AuthenticationResponseDto authenticate(@RequestBody AuthenticationRequestDto authenticationRequest) {
        return authenticationService.authenticate(authenticationRequest);
    }

}
