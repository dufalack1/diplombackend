package by.grsu.eventlink.service;

import by.grsu.eventlink.dto.common.GenericMessageDto;
import by.grsu.eventlink.dto.invitation.InvitationRequestDto;

public interface InvitationService {

    GenericMessageDto mailResend(InvitationRequestDto invitationRequest);

    GenericMessageDto invitationRequest(InvitationRequestDto invitationRequest);

}
