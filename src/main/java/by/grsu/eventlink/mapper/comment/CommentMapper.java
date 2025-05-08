package by.grsu.eventlink.mapper.comment;

import by.grsu.eventlink.dto.comment.CommentDto;
import by.grsu.eventlink.entity.Comment;
import by.grsu.eventlink.mapper.user.UserMapper;
import liquibase.repackaged.org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CommentMapper {

    public static List<CommentDto> mapToDto(List<Comment> comments) {
        return CollectionUtils.emptyIfNull(comments).stream()
                .map(CommentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public static CommentDto mapToDto(Comment comment) {
        comment = Optional.ofNullable(comment)
                .orElse(Comment.builder().build());

        return CommentDto.builder()
                .id(comment.getId())
                .message(comment.getMessage())
                .publicationTime(comment.getPublicationTime())
                .author(UserMapper.mapToTruncatedDto(comment.getAuthor()))
                .nodeId(comment.getNode().getId())
                .build();
    }

    public static Comment mapToDo(CommentDto commentDto) {
        commentDto = Optional.ofNullable(commentDto)
                .orElse(CommentDto.builder().build());

        return Comment.builder()
                .message(commentDto.getMessage())
                .build();
    }

}
