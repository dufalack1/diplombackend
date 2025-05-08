package by.grsu.eventlink.service;

import by.grsu.eventlink.dto.nodeinvitation.NodeInvitationDto;

import java.util.List;

public interface NodeInvitationService {

    List<NodeInvitationDto> getNodeInvitationsByUserId(Long userId);

}
