package by.grsu.eventlink.service;

import by.grsu.eventlink.dto.user.UserNodesDto;
import by.grsu.eventlink.dto.common.GenericMessageDto;
import by.grsu.eventlink.dto.node.GenericBufferDto;
import by.grsu.eventlink.dto.node.NodeDto;
import by.grsu.eventlink.dto.node.NodeSearchingAttributesDto;
import by.grsu.eventlink.dto.node.TruncatedNodeDto;
import org.springframework.data.domain.Page;

public interface NodeService {

    GenericMessageDto delete(Long id);

    NodeDto getNodeById(Long id, String email);

    GenericMessageDto registerEvent(NodeDto nodeDto);

    UserNodesDto getNodesConnectedWithUser(Long userId);

    GenericMessageDto requestNodeJoin(String email, Long nodeId);

    GenericMessageDto invitePeople(GenericBufferDto genericBufferDto);

    GenericMessageDto acceptJoinInvitation(String email, Long nodeId);

    GenericMessageDto cancelInvitation(Long nodeId, Long userId);

    GenericMessageDto approveNodeJoin(Long nodeId, Long invitedId);

    GenericMessageDto removeJoinedPeople(GenericBufferDto genericBufferDto);

    Page<TruncatedNodeDto> getTruncatedNodesByAttributes(NodeSearchingAttributesDto searchingAttributes,
                                                         Integer page, Integer size);

}
