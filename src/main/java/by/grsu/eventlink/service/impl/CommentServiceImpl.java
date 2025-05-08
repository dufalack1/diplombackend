package by.grsu.eventlink.service.impl;

import by.grsu.eventlink.dto.comment.CommentDto;
import by.grsu.eventlink.dto.comment.EditableCommentDto;
import by.grsu.eventlink.entity.Comment;
import by.grsu.eventlink.entity.Node;
import by.grsu.eventlink.entity.User;
import by.grsu.eventlink.exception.comment.CommentNotFoundException;
import by.grsu.eventlink.exception.comment.EmptyCommentException;
import by.grsu.eventlink.exception.comment.SpamCommentException;
import by.grsu.eventlink.exception.node.NodeNotFoundException;
import by.grsu.eventlink.exception.user.UserNotFoundException;
import by.grsu.eventlink.mapper.comment.CommentMapper;
import by.grsu.eventlink.repository.CommentRepository;
import by.grsu.eventlink.repository.NodeRepository;
import by.grsu.eventlink.repository.UserRepository;
import by.grsu.eventlink.service.CommentService;
import by.grsu.eventlink.util.collection.Collections;
import by.grsu.eventlink.util.helper.Conditional;
import liquibase.repackaged.org.apache.commons.lang3.StringUtils;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;

    private final NodeRepository nodeRepository;

    private final CommentRepository commentRepository;

    @Override
    public void deleteComment(Long id) {
        Conditional.fromBoolean(commentRepository.existsById(id))
                .ifFalseThenThrow(() -> new CommentNotFoundException(id));

        commentRepository.deleteById(id);
    }

    @Override
    public List<CommentDto> getCommentsByNodeId(Long nodeId) {
        Conditional.fromBoolean(nodeRepository.existsById(nodeId))
                .ifFalseThenThrow(() -> new NodeNotFoundException(nodeId));

        List<Comment> comments = commentRepository.getCommentsByNode_Id(nodeId);


        Collections.ifEmptyThenThrow(comments);

        return CommentMapper.mapToDto(comments);
    }

    @Override
    public CommentDto leftNodeComment(CommentDto commentDto) {
        Node currentNode = nodeRepository.getNodeById(commentDto.getNodeId())
                .orElseThrow(() -> new NodeNotFoundException(commentDto.getNodeId()));
        User commentAuthor = userRepository.getUserById(commentDto.getAuthorId())
                .orElseThrow(() -> new UserNotFoundException(commentDto.getAuthorId()));

        validate(commentDto);

        Comment comment = CommentMapper.mapToDo(commentDto);

        comment.setNode(currentNode);
        comment.setAuthor(commentAuthor);
        comment.setPublicationTime(LocalDateTime.now());

        commentRepository.save(comment);

        return CommentMapper.mapToDto(comment);
    }

    @Override
    public CommentDto editCommentMessage(Long commentId, EditableCommentDto editableCommentDto) {
        Conditional.fromBoolean(StringUtils.isEmpty(editableCommentDto.getMessage()))
                .ifTrueThenThrow(EmptyCommentException::new);

        Comment editCandidate = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        editCandidate.setMessage(editableCommentDto.getMessage());
        commentRepository.save(editCandidate);

        return CommentMapper.mapToDto(editCandidate);
    }

    private void validate(CommentDto commentDto) {
        Conditional.fromBoolean(Strings.isEmpty(commentDto.getMessage()))
                .ifTrueThenThrow(EmptyCommentException::new);

        Optional<Comment> comment = commentRepository.
                getByMessageAndNode_IdAndAuthor_Id(commentDto.getMessage(),
                        commentDto.getNodeId(), commentDto.getAuthorId());

        comment.ifPresent(rep -> {
            if (rep.getPublicationTime().plusHours(1).isAfter(LocalDateTime.now()))
                throw new SpamCommentException();
        });
    }

}
