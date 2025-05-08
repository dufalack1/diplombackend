package by.grsu.eventlink.service.impl;

import by.grsu.eventlink.configuration.properties.enums.StorageCase;
import by.grsu.eventlink.dto.user.UserNodesDto;
import by.grsu.eventlink.dto.common.GenericMessageDto;
import by.grsu.eventlink.dto.enums.Entity;
import by.grsu.eventlink.dto.node.GenericBufferDto;
import by.grsu.eventlink.dto.node.NodeDto;
import by.grsu.eventlink.dto.node.NodeSearchingAttributesDto;
import by.grsu.eventlink.dto.node.TruncatedNodeDto;
import by.grsu.eventlink.entity.JoinRequest;
import by.grsu.eventlink.entity.Node;
import by.grsu.eventlink.entity.SubCategory;
import by.grsu.eventlink.entity.User;
import by.grsu.eventlink.exception.common.ValidationException;
import by.grsu.eventlink.exception.joinrequest.JoinRequestAlreadyPresentException;
import by.grsu.eventlink.exception.joinrequest.JoinRequestNotFoundException;
import by.grsu.eventlink.exception.joinrequest.NodeNotInvitedException;
import by.grsu.eventlink.exception.node.NodeAlreadyJoinedException;
import by.grsu.eventlink.exception.node.NodeExpiredException;
import by.grsu.eventlink.exception.node.NodeNotFoundException;
import by.grsu.eventlink.exception.node.NodeRequiredPeopleOverflowException;
import by.grsu.eventlink.exception.subcategory.SubCategoryNotFoundException;
import by.grsu.eventlink.exception.user.AgeLimitException;
import by.grsu.eventlink.exception.user.UserNotFoundException;
import by.grsu.eventlink.mail.Postman;
import by.grsu.eventlink.mapper.node.NodeMapper;
import by.grsu.eventlink.repository.JoinRequestRepository;
import by.grsu.eventlink.repository.NodeRepository;
import by.grsu.eventlink.repository.SubCategoryRepository;
import by.grsu.eventlink.repository.UserRepository;
import by.grsu.eventlink.service.NodeService;
import by.grsu.eventlink.service.util.StorageHelper;
import by.grsu.eventlink.util.collection.Collections;
import by.grsu.eventlink.util.collection.PageUtils;
import by.grsu.eventlink.util.date.DateUtils;
import by.grsu.eventlink.util.helper.Conditional;
import by.grsu.eventlink.util.plug.DummyUtils;
import by.grsu.eventlink.util.storage.Extractors;
import liquibase.repackaged.org.apache.commons.lang3.StringUtils;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NodeServiceImpl implements NodeService {

    private final StorageHelper storageHelper;

    private final UserRepository userRepository;

    private final NodeRepository nodeRepository;

    private final Postman nodeInvitationPostman;

    private final SubCategoryRepository subCategoryRepository;

    private final JoinRequestRepository joinRequestRepository;

    @Override
    @Transactional(readOnly = true)
    public NodeDto getNodeById(Long id, String email) {
        Node foundedNode = nodeRepository.getNodeById(id)
                .orElseThrow(() -> new NodeNotFoundException(id));

        if (Strings.EMPTY.equals(email) || !nodeRepository.isJoinedOrOwnerByEmail(email, id)) {
            foundedNode.setJoinRequests(new ArrayList<>());
            foundedNode.setJoinedUsers(new ArrayList<>());
            foundedNode.setPlace(Extractors.extractCityFromPlace(foundedNode.getPlace()));
        }

        return setAvatarsAndGet(NodeMapper.mapNodeDoToDto(foundedNode));
    }

    @Override
    @Transactional
    public GenericMessageDto invitePeople(GenericBufferDto genericBufferDto) {//пригласить пользователя
        User invitationCandidate = userRepository.findById(genericBufferDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(genericBufferDto.getUserId()));

        Node currentNode = nodeRepository.getNodeById(genericBufferDto.getNodeId())
                .orElseThrow(() -> new NodeNotFoundException(genericBufferDto.getNodeId()));

        joinRequestRepository.findByInvitedIdAndNodeId(genericBufferDto.getUserId(), genericBufferDto.getNodeId())
                        .ifPresent(opt -> {
                            throw new JoinRequestAlreadyPresentException(genericBufferDto.getUserId(),
                                    genericBufferDto.getNodeId());
                        });

        Conditional.fromBoolean(currentNode.getJoinedUsers().stream()
                .anyMatch(usr -> usr.getId().equals(genericBufferDto.getUserId())) ||
                        currentNode.getOwner().getId().equals(genericBufferDto.getUserId()))
                .ifTrueThenThrow(NodeAlreadyJoinedException::new);

        Conditional.fromBoolean(currentNode.getJoinedUsers().size() + 1 > currentNode.getRequiredPeople())
                .ifTrueThenThrow(() -> new NodeRequiredPeopleOverflowException(currentNode.getRequiredPeople()));

        Conditional.fromBoolean(currentNode.getAgeLimit() > DateUtils.calculateAge(invitationCandidate.getBirthDate()))
                .ifTrueThenThrow(AgeLimitException::new);

        Conditional.fromBoolean(currentNode.getExpirationTime().before(DateUtils.getDateNow()))
                .ifTrueThenThrow(() -> new NodeExpiredException(genericBufferDto.getNodeId()));

        JoinRequest joinRequest = JoinRequest.builder()
                .node(currentNode)
                .isInvited(Boolean.TRUE)
                .invited(invitationCandidate)
                .build();

        joinRequestRepository.save(joinRequest);

        nodeInvitationPostman.send(invitationCandidate.getCredentials().getEmail(),
                List.of(invitationCandidate.getDisplayedName(), currentNode.getTitle(), currentNode.getDescription(),
                        currentNode.getAgeLimit().toString()));

        return GenericMessageDto.builder()
                .message("User was successfully invited")
                .build();
    }

    @Override
    @Transactional
    public GenericMessageDto requestNodeJoin(String email, Long nodeId) {//отправить запрос на присоединение от лица пользователя который хочет вступить
        User joinCandidate = userRepository.findByCredentials_Email(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        Node currentNode = nodeRepository.getNodeById(nodeId)
                .orElseThrow(() -> new NodeNotFoundException(nodeId));

        joinRequestRepository.findByInvitedIdAndNodeId(joinCandidate.getId(), currentNode.getId())
                .ifPresent(opt -> {
                    throw new JoinRequestAlreadyPresentException(joinCandidate.getId(), currentNode.getId());
                });


        Conditional.fromBoolean(currentNode.getAgeLimit() > DateUtils.calculateAge(joinCandidate.getBirthDate()))
                .ifTrueThenThrow(AgeLimitException::new);

        Conditional.fromBoolean(currentNode.getExpirationTime().before(DateUtils.getDateNow()))
                .ifTrueThenThrow(() -> new NodeExpiredException(nodeId));

        JoinRequest joinRequest = JoinRequest.builder()
                .node(currentNode)
                .isInvited(Boolean.TRUE)
//                .isInvited(Boolean.FALSE)
                .invited(joinCandidate)
                .build();

        joinRequestRepository.save(joinRequest);

        return GenericMessageDto.builder()
                .message("Join request was successfully created")
                .build();
    }

    @Override
    @Transactional
    public GenericMessageDto acceptJoinInvitation(String email, Long nodeId) {//принять приглашение
        User acceptanceCandidate = userRepository.findByCredentials_Email(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        JoinRequest joinRequest = joinRequestRepository.findByInvitedIdAndNodeId(acceptanceCandidate.getId(), nodeId)
                .orElseThrow(() -> new JoinRequestNotFoundException(email, nodeId));

        Node currentNode = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new NodeNotFoundException(nodeId));

        if (!joinRequest.getIsInvited() || !currentNode.getId().equals(joinRequest.getNode().getId())
            || !acceptanceCandidate.getId().equals(joinRequest.getInvited().getId())) {
            throw new NodeNotInvitedException(acceptanceCandidate.getId(), nodeId);
        } else if (currentNode.getJoinedUsers().size() + 1 > currentNode.getRequiredPeople()) {
            throw new NodeRequiredPeopleOverflowException(currentNode.getRequiredPeople());
        }

        currentNode.getJoinedUsers().add(acceptanceCandidate);
        acceptanceCandidate.getJoinedEvents().add(currentNode);

        nodeRepository.save(currentNode);

        joinRequestRepository.delete(joinRequest);

        return GenericMessageDto.builder()
                .message("You was successfully accept the invitation")
                .build();
    }

    @Override
    @Transactional
    public GenericMessageDto cancelInvitation(Long nodeId, Long userId) {
        JoinRequest joinRequest = joinRequestRepository.findByInvitedIdAndNodeId(userId, nodeId)
                .orElseThrow(() -> new NodeNotInvitedException(userId, nodeId));

        joinRequestRepository.delete(joinRequest);

        return GenericMessageDto.builder()
                .message("You're successfully decline/remove invitation/join request")
                .build();
    }

    @Override
    public GenericMessageDto approveNodeJoin(Long nodeId, Long invitedId) {//одобрить запрос на присоединение
        User acceptanceCandidate = userRepository.getUserById(invitedId)
                .orElseThrow(() -> new UserNotFoundException(invitedId));

        JoinRequest joinRequest = joinRequestRepository.findByInvitedIdAndNodeId(acceptanceCandidate.getId(), nodeId)
                .orElseThrow(() -> new JoinRequestNotFoundException(invitedId, nodeId));

        Node currentNode = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new NodeNotFoundException(nodeId));

        if (joinRequest.getIsInvited() || !currentNode.getId().equals(joinRequest.getNode().getId())
                || !acceptanceCandidate.getId().equals(joinRequest.getInvited().getId())) {
            throw new NodeNotInvitedException(acceptanceCandidate.getId(), nodeId);
        } else if (currentNode.getJoinedUsers().size() + 1 > currentNode.getRequiredPeople()) {
            throw new NodeRequiredPeopleOverflowException(currentNode.getRequiredPeople());
        }

        currentNode.getJoinedUsers().add(acceptanceCandidate);
        acceptanceCandidate.getJoinedEvents().add(currentNode);

        nodeRepository.save(currentNode);

        joinRequestRepository.delete(joinRequest);

        return GenericMessageDto.builder()
                .message("You was successfully approve the invitation")
                .build();
    }

    @Override
    public UserNodesDto getNodesConnectedWithUser(Long userId) {
        List<Node> owned = nodeRepository.getNodesOwnedByUser(userId);
        List<Node> joined = nodeRepository.getNodesUserJoined(userId);

        Collections.ifEmptyThenThrow(owned, joined);

        return UserNodesDto.builder()
                .owned(NodeMapper.mapToDto(owned))
                .joined(NodeMapper.mapToDto(joined))
                .build();
    }

    @Override
    @Transactional
    public GenericMessageDto removeJoinedPeople(GenericBufferDto genericBufferDto) {//кикнуть пользователя(пользователь должен принять приглашение)
        Conditional.fromBoolean(nodeRepository
                        .isUserJoined(genericBufferDto.getNodeId(), genericBufferDto.getUserId()))
                        .ifFalseThenThrow(() -> new NodeNotInvitedException(genericBufferDto.getUserId(),
                                genericBufferDto.getNodeId()));

        Conditional.fromBoolean(userRepository.existsById(genericBufferDto.getUserId()))
                .ifFalseThenThrow(() -> new UserNotFoundException(genericBufferDto.getUserId()));

        Conditional.fromBoolean(nodeRepository.existsById(genericBufferDto.getNodeId()))
                .ifFalseThenThrow(() -> new NodeNotFoundException(genericBufferDto.getNodeId()));

        nodeRepository.removeJoinedUser(genericBufferDto.getNodeId(), genericBufferDto.getUserId());

        return GenericMessageDto.builder()
                .message("User was successfully uninvited")
                .build();
    }

    @Override
    public GenericMessageDto registerEvent(NodeDto nodeDto) {
        validate(nodeDto);

        User linkedUser = userRepository.findById(nodeDto.getOwnerId())
                .orElseThrow(() -> new UserNotFoundException(nodeDto.getId()));

        SubCategory linkedSubCategory = subCategoryRepository.findById(nodeDto.getSubCategoryId())
                .orElseThrow(() -> new SubCategoryNotFoundException(nodeDto.getSubCategoryId()));

        Node registerCandidate = NodeMapper.mapNodeDtoToDo(nodeDto);

        registerCandidate.setOwner(linkedUser);
        registerCandidate.setSubCategory(linkedSubCategory);
        registerCandidate.setPublishTime(DateUtils.getDateNow());
        registerCandidate.setExpirationTime(nodeDto.getStartDate());

        nodeRepository.save(registerCandidate);

        return GenericMessageDto.builder()
                .entityType(Entity.NODE)
                .entityId(registerCandidate.getId())
                .message("Event successfully registered")
                .build();
    }

    @Override
    public Page<TruncatedNodeDto> getTruncatedNodesByAttributes(NodeSearchingAttributesDto searchingAttributes,//отображения страниц по введенным атрибутам
                                                                Integer page, Integer size) {
        Page<Node> foundedNodes;

        if (Optional.ofNullable(searchingAttributes.getSubCategoryId()).isPresent() &&
                Optional.ofNullable(searchingAttributes.getTitleContains()).isPresent()) {
            foundedNodes = nodeRepository.findNodesBySubCategory_IdAndTitleContainingIgnoreCase(
                    searchingAttributes.getSubCategoryId(), searchingAttributes.getTitleContains(),
                    PageUtils.getPage(page, size)
            );
        } else if (Optional.ofNullable(searchingAttributes.getSubCategoryId()).isPresent() &&
                Optional.ofNullable(searchingAttributes.getDescriptionContains()).isPresent()) {
            foundedNodes = nodeRepository.findNodesBySubCategory_IdAndDescriptionContainingIgnoreCase(
                    searchingAttributes.getSubCategoryId(), searchingAttributes.getDescriptionContains(),
                    PageUtils.getPage(page, size)
            );
        } else if (Optional.ofNullable(searchingAttributes.getSubCategoryId()).isPresent()) {
            foundedNodes = nodeRepository.findNodesBySubCategory_Id(searchingAttributes.getSubCategoryId(),
                    PageUtils.getPage(page, size));
        } else {
            foundedNodes = nodeRepository.findNodesByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                    Optional.ofNullable(searchingAttributes.getTitleContains()).orElse(DummyUtils.DUMMY_PLUG),
                    Optional.ofNullable(searchingAttributes.getDescriptionContains()).orElse(DummyUtils.DUMMY_PLUG),
                    PageUtils.getPage(page, size)
            );
        }

        Collections.ifEmptyThenThrow(foundedNodes);

        return NodeMapper.mapNodeDoToTruncatedNodeDto(foundedNodes);
    }

    @Override
//    public GenericMessageDto delete(Long id) {
//        Conditional.fromBoolean(nodeRepository.existsById(id))
//                .ifFalseThenThrow(() -> new NodeNotFoundException(id));
//
//        nodeRepository.deleteById(id);
//
//        return GenericMessageDto.builder()
//                .message("Node was successfully deleted")
//                .build();
//    }
    public GenericMessageDto delete(Long id) {
        var node = nodeRepository.findById(id).orElseThrow(() -> new NodeNotFoundException(id));
        for (User user : node.getJoinedUsers()) {
            user.getJoinedEvents().remove(node);
        }
        node.getJoinedUsers().clear();
        nodeRepository.deleteById(id);

        return GenericMessageDto.builder()
                .message("Node was successfully deleted")
                .build();
    }

    private NodeDto setAvatarsAndGet(NodeDto mappedNode) {
        mappedNode.getJoinRequests().forEach(joinRequest ->
                joinRequest.setAvatarUrl(storageHelper.constructUrl(joinRequest.getAvatarUrl(), StorageCase.USER)));

        mappedNode.getJoinedPeople().forEach(joinedPeople ->
                joinedPeople.setAvatarUrl(storageHelper.constructUrl(joinedPeople.getAvatarUrl(), StorageCase.USER)));

        mappedNode.getOwner().setAvatarUrl(storageHelper.constructUrl(
                mappedNode.getOwner().getAvatarUrl(), StorageCase.USER));

        mappedNode.getComments().forEach(comment ->
                comment.getAuthor().setAvatarUrl(storageHelper.constructUrl(
                        comment.getAuthor().getAvatarUrl(), StorageCase.USER)));

        mappedNode.getSubCategory().setImageUrl(
                storageHelper.constructUrl(mappedNode.getSubCategory().getImageUrl(), StorageCase.SUB_CATEGORY)
        );

        return mappedNode;
    }

    private void validate(NodeDto nodeDto) {
        Conditional.fromBoolean(Objects.nonNull(nodeDto.getTitle()) &&
                        StringUtils.isEmpty(nodeDto.getTitle()))
                .ifTrueThenThrow(() -> new ValidationException("Title"));

        Conditional.fromBoolean(Objects.nonNull(nodeDto.getDescription()) &&
                        StringUtils.isEmpty(nodeDto.getDescription()))
                .ifTrueThenThrow(() -> new ValidationException("Description"));

        Conditional.fromBoolean(Objects.nonNull(nodeDto.getPlace()) &&
                        StringUtils.isEmpty(nodeDto.getPlace()))
                .ifTrueThenThrow(() -> new ValidationException("Place"));

        Conditional.fromBoolean(Objects.nonNull(nodeDto.getAgeLimit()) &&
                (nodeDto.getAgeLimit() < 18 || nodeDto.getAgeLimit() > 150))
                .ifTrueThenThrow(() -> new ValidationException("Age Limit (18-150)"));

        Conditional.fromBoolean(Objects.nonNull(nodeDto.getRequiredPeople()) &&
                        (nodeDto.getRequiredPeople() < 2 || nodeDto.getRequiredPeople() > 30))
                .ifTrueThenThrow(() -> new ValidationException("Required people (2-30)"));

        Conditional.fromBoolean(Objects.isNull(nodeDto.getOwnerId()))
                .ifTrueThenThrow(() -> new ValidationException("Owner Id"));

        Conditional.fromBoolean(Objects.isNull(nodeDto.getSubCategoryId()))
                .ifTrueThenThrow(() -> new ValidationException("Sub Category Id"));

        Conditional.fromBoolean(Objects.nonNull(nodeDto.getStartDate()) &&
                DateUtils.getDateNow().after(nodeDto.getStartDate()))
                .ifTrueThenThrow(() -> new ValidationException("Start Date"));
    }

}
