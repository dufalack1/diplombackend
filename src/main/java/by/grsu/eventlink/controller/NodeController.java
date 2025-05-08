package by.grsu.eventlink.controller;

import by.grsu.eventlink.controller.documentation.NodeControllerDoc;
import by.grsu.eventlink.dto.user.UserNodesDto;
import by.grsu.eventlink.dto.comment.CommentDto;
import by.grsu.eventlink.dto.common.GenericMessageDto;
import by.grsu.eventlink.dto.node.GenericBufferDto;
import by.grsu.eventlink.dto.node.NodeDto;
import by.grsu.eventlink.dto.node.NodeSearchingAttributesDto;
import by.grsu.eventlink.dto.node.TruncatedNodeDto;
import by.grsu.eventlink.service.CommentService;
import by.grsu.eventlink.service.NodeService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/nodes")
public class NodeController implements NodeControllerDoc {

    private final NodeService nodeService;

    private final CommentService commentService;

    @Override
    @GetMapping("{id}")
    public NodeDto getById(@PathVariable Long id, Authentication authentication) {
        return nodeService.getNodeById(id, Objects.isNull(authentication) ? Strings.EMPTY : authentication.getName());
    }

    @Override
    @DeleteMapping("{id}")
    @PreAuthorize("@accessManagerService.isAuthorisedAdmittedToNode(#id, authentication)")
    public GenericMessageDto delete(@PathVariable Long id) {
        return nodeService.delete(id);
    }

    @Override
    @PostMapping
    public GenericMessageDto registerEvent(@RequestBody NodeDto nodeDto) {//создание ивента
        return nodeService.registerEvent(nodeDto);
    }

    @Override
    @PostMapping("{id}/invite")//пригласить пользователя
    @PreAuthorize("@accessManagerService.isAuthorisedAdmittedToNode(#id, authentication)")
    public GenericMessageDto invitePeople(@PathVariable Long id, @RequestBody GenericBufferDto bufferDto) {
        bufferDto.setNodeId(id);
        return nodeService.invitePeople(bufferDto);
    }

    @Override
    @PostMapping("{id}/join")//отправить запрос на присоединение от лица пользователя который хочет вступить
    public GenericMessageDto requestNodeJoin(@PathVariable Long id, Authentication authentication) {
        return nodeService.requestNodeJoin(authentication.getName(), id);
    }

    @Override
    @PatchMapping("{id}/uninvited")//кикнуть пользователя(пользователь должен принять приглашение)
    @PreAuthorize("@accessManagerService.isAuthorisedAdmittedToNode(#id, authentication)")
    public GenericMessageDto removeJoinedPeople(@PathVariable Long id, @RequestBody GenericBufferDto genericBufferDto) {
        genericBufferDto.setNodeId(id);
        return nodeService.removeJoinedPeople(genericBufferDto);
    }

    @Override
    @PostMapping("{id}/accept")//принять приглашение
    public GenericMessageDto acceptJoinInvitation(@PathVariable Long id, Authentication authentication) {
        return nodeService.acceptJoinInvitation(authentication.getName(), id);
    }

    @PostMapping("{nodeId}/approve/{invitedId}")//одобрить запрос на присоединение
    public GenericMessageDto approveNodeJoin(@PathVariable Long nodeId, @PathVariable Long invitedId) {
        return nodeService.approveNodeJoin(nodeId, invitedId);
    }

    @Override
    @DeleteMapping("{id}/reject")//отклонить приглашение(как со стороны того кто пригласил, так и со стороны того кто приглашен
    public GenericMessageDto cancelInvitation(@PathVariable Long id, @RequestBody GenericBufferDto bufferDto) {
        return nodeService.cancelInvitation(id, bufferDto.getUserId());
    }

    @Override
    @GetMapping
    public Page<TruncatedNodeDto> getTruncatedNodesByAttributes(@RequestParam("page") @NotNull Integer page,//отображения страниц по введенным атрибутам
                                                                @RequestParam("size") @NotNull Integer size,
                                                                @RequestParam(value = "subCID", required = false) Long subCategoryId,
                                                                @RequestParam(value = "ttlCntn", required = false) String titleContains,
                                                                @RequestParam(value = "dscrCntn", required = false) String descriptionContains) {
        return nodeService.getTruncatedNodesByAttributes(NodeSearchingAttributesDto.builder()
                .subCategoryId(subCategoryId)
                .titleContains(titleContains)
                .descriptionContains(descriptionContains)
                .build(), page, size);
    }

    @PutMapping("/comments")//отправить сообщение в ивенте
    public CommentDto leaveComment(@RequestBody CommentDto commentDto) {
        return commentService.leftNodeComment(commentDto);
    }

    @GetMapping("/users/{userId}")
    @PreAuthorize("@accessManagerService.isAuthorisedAdmittedToUser(#userId, authentication)")
    public UserNodesDto getNodesConnectedWithUser(@PathVariable Long userId) {
        return nodeService.getNodesConnectedWithUser(userId);
    }

}
