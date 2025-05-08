package by.grsu.eventlink.mapper;

import by.grsu.eventlink.dto.nodeinvitation.NodeInvitationDto;
import by.grsu.eventlink.entity.JoinRequest;

import java.util.List;
import java.util.stream.Collectors;

public class NodeInvitationMapper {

    public static List<NodeInvitationDto> mapToDto(List<JoinRequest> joinRequests, Long userId) {
        return joinRequests.stream()
                .map(request -> NodeInvitationMapper.mapToDto(request, userId))
                .collect(Collectors.toList());
    }

    public static NodeInvitationDto mapToDto(JoinRequest joinRequest, Long userId) {
        return NodeInvitationDto.builder()
                .invitedId(joinRequest.getInvited().getId())
                .id(joinRequest.getId())
                .nodeId(joinRequest.getNode().getId())
                .isInvited(joinRequest.getIsInvited())
                .build();
    }

}
