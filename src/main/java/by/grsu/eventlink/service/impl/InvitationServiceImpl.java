package by.grsu.eventlink.service.impl;

import by.grsu.eventlink.dto.common.GenericMessageDto;
import by.grsu.eventlink.dto.invitation.InvitationRequestDto;
import by.grsu.eventlink.entity.Invitation;
import by.grsu.eventlink.exception.common.NotFoundException;
import by.grsu.eventlink.exception.invitation.ActiveInvitationAlreadyExist;
import by.grsu.eventlink.exception.user.UserAlreadyExistException;
import by.grsu.eventlink.mail.Postman;
import by.grsu.eventlink.repository.CredentialRepository;
import by.grsu.eventlink.repository.InvitationRepository;
import by.grsu.eventlink.service.InvitationService;
import by.grsu.eventlink.util.date.DateUtils;
import by.grsu.eventlink.util.helper.CodeGen;
import by.grsu.eventlink.util.helper.Conditional;
import by.grsu.eventlink.util.plug.DummyUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvitationServiceImpl implements InvitationService {

    private final Postman invitationPostman;

    private final CredentialRepository credentialRepository;

    private final InvitationRepository invitationRepository;

    @Override
    public GenericMessageDto mailResend(InvitationRequestDto invitationRequest) {
        Invitation currentInvitation = invitationRepository.getInvitationByEmail(invitationRequest.getEmail())
                .orElseThrow(() -> new NotFoundException("Invitation was never sent"));

        String approvalCode = CodeGen.generateSixDigits();

        currentInvitation.setIsActive(true);
        currentInvitation.setGeneratedCode(approvalCode);
        currentInvitation.setExpirationTime(DateUtils.getDatePlusDays(7));

        invitationRepository.save(currentInvitation);

        invitationPostman.send(invitationRequest.getEmail(), List.of(
                invitationRequest.getEmail().split("@")[DummyUtils.ZERO], approvalCode
        ));

        return GenericMessageDto.builder()
                .message("Invitation was resent to you, check your e-mail")
                .build();
    }

    @Override
    public GenericMessageDto invitationRequest(InvitationRequestDto invitationRequest) {
        Conditional.fromBoolean(credentialRepository.existsByEmail(invitationRequest.getEmail()))
                .ifTrueThenThrow(UserAlreadyExistException::new);

        Conditional.fromBoolean(invitationRepository.existsByEmailAndExpirationTimeAfter(invitationRequest.getEmail(),
                        DateUtils.getDateNow()))
                .ifTrueThenThrow(ActiveInvitationAlreadyExist::new);

        String approvalCode = CodeGen.generateSixDigits();

        Invitation currentInvitation = Invitation.builder()
                .isActive(true)
                .email(invitationRequest.getEmail())
                .generatedCode(approvalCode)
                .expirationTime(DateUtils.getDatePlusDays(7))
                .build();

        invitationRepository.save(currentInvitation);

        invitationPostman.send(invitationRequest.getEmail(), List.of(
                invitationRequest.getEmail().split("@")[DummyUtils.ZERO], approvalCode
        ));

        log.info(String.format("Email: %s, Invitation code: %s", currentInvitation.getEmail(),
                currentInvitation.getGeneratedCode()));

        return GenericMessageDto.builder()
                .message("Everything is ok, check your e-mail")
                .build();
    }

}
