package by.grsu.eventlink.mapper.node;

import by.grsu.eventlink.dto.node.NodeDto;
import by.grsu.eventlink.dto.node.TruncatedNodeDto;
import by.grsu.eventlink.entity.JoinRequest;
import by.grsu.eventlink.entity.Node;
import by.grsu.eventlink.mapper.SubCategoryMapper;
import by.grsu.eventlink.mapper.comment.CommentMapper;
import by.grsu.eventlink.mapper.user.UserMapper;
import by.grsu.eventlink.util.collection.PageUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class NodeMapper {

    public static NodeDto mapNodeDoToDto(Node nodeDo) {
        nodeDo = Optional.ofNullable(nodeDo)
                .orElse(Node.builder().build());

        return NodeDto.builder()
                .id(nodeDo.getId())
                .title(nodeDo.getTitle())
                .place(nodeDo.getPlace())
                .ageLimit(nodeDo.getAgeLimit())
                .description(nodeDo.getDescription())
                .startDate(nodeDo.getExpirationTime())
                .requiredPeople(nodeDo.getRequiredPeople())
                .owner(UserMapper.mapToDto(nodeDo.getOwner()))
                .comments(CommentMapper.mapToDto(nodeDo.getComments()))
                .joinedPeople(UserMapper.mapToTruncatedDto(nodeDo.getJoinedUsers()))
                .joinRequests(UserMapper.mapToTruncatedDto(nodeDo.getJoinRequests().stream()
                        .map(JoinRequest::getInvited).collect(Collectors.toList())))
                .subCategory(SubCategoryMapper.mapToDto(nodeDo.getSubCategory()))
                .build();
    }

    public static Node mapNodeDtoToDo(NodeDto nodeDto) {
        nodeDto = Optional.ofNullable(nodeDto)
                .orElse(NodeDto.builder().build());

        return Node.builder()
                .title(nodeDto.getTitle())
                .place(nodeDto.getPlace())
                .ageLimit(nodeDto.getAgeLimit())
                .description(nodeDto.getDescription())
                .requiredPeople(nodeDto.getRequiredPeople())
                .build();
    }

    public static TruncatedNodeDto mapNodeDoToTruncatedNodeDto(Node nodeDo) {
        nodeDo = Optional.ofNullable(nodeDo).orElse(Node.builder().build());

        return TruncatedNodeDto.builder()
                .id(nodeDo.getId())
                .title(nodeDo.getTitle())
                .place(nodeDo.getPlace())
                .ownerId(nodeDo.getOwner().getId())
                .description(nodeDo.getDescription())
                .requiredPeople(nodeDo.getRequiredPeople())
                .build();
    }

    public static Page<TruncatedNodeDto> mapNodeDoToTruncatedNodeDto(Page<Node> nodeDoList) {
        return new PageImpl<>(PageUtils.emptyIfNull(nodeDoList).stream()
                .map(node -> NodeMapper.mapNodeDoToTruncatedNodeDto((Node) node))
                .collect(Collectors.toList()));
    }

    public static List<TruncatedNodeDto> mapToDto(List<Node> nodes) {
        return nodes.stream()
                .map(NodeMapper::mapNodeDoToTruncatedNodeDto)
                .collect(Collectors.toList());
    }

}
