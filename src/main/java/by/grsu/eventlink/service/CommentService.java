package by.grsu.eventlink.service;

import by.grsu.eventlink.dto.comment.CommentDto;
import by.grsu.eventlink.dto.comment.EditableCommentDto;

import java.util.List;

public interface CommentService {

    void deleteComment(Long id);

    List<CommentDto> getCommentsByNodeId(Long nodeId);

    CommentDto leftNodeComment(CommentDto commentDto);

    CommentDto editCommentMessage(Long commentId, EditableCommentDto editableCommentDto);

}
