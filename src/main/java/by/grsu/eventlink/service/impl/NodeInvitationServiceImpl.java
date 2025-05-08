package by.grsu.eventlink.service.impl;

import by.grsu.eventlink.dto.nodeinvitation.NodeInvitationDto;
import by.grsu.eventlink.entity.JoinRequest;
import by.grsu.eventlink.mapper.NodeInvitationMapper;
import by.grsu.eventlink.repository.JoinRequestRepository;
import by.grsu.eventlink.service.NodeInvitationService;
import by.grsu.eventlink.util.collection.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NodeInvitationServiceImpl implements NodeInvitationService {

    private final JoinRequestRepository joinRequestRepository;

    @Override
    public List<NodeInvitationDto> getNodeInvitationsByUserId(Long userId) {
        List<JoinRequest> invitations = joinRequestRepository.findAllByInvitedId(userId);
        invitations.addAll(joinRequestRepository.findAllByNode_Owner_Id(userId));

        Collections.ifEmptyThenThrow(invitations);

        return NodeInvitationMapper.mapToDto(invitations, userId);
    }

}
