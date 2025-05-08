package by.grsu.eventlink.controller;

import by.grsu.eventlink.controller.documentation.InvitationControllerDoc;
import by.grsu.eventlink.dto.common.GenericMessageDto;
import by.grsu.eventlink.dto.invitation.InvitationRequestDto;
import by.grsu.eventlink.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/invitations")
public class InvitationController implements InvitationControllerDoc {

    private final InvitationService invitationService;

    @Override
    @PostMapping("/resend")
    public GenericMessageDto mailResend(@RequestBody InvitationRequestDto invitationRequestDto) {
        return invitationService.mailResend(invitationRequestDto);
    }

    @Override
    @PostMapping
    public GenericMessageDto invitationRequest(@RequestBody @Validated InvitationRequestDto invitationRequestDto) {
        return invitationService.invitationRequest(invitationRequestDto);
    }

}
