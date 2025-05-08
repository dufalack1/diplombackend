package by.grsu.eventlink.controller;

import by.grsu.eventlink.controller.documentation.CommentControllerDoc;
import by.grsu.eventlink.dto.comment.CommentDto;
import by.grsu.eventlink.dto.comment.EditableCommentDto;
import by.grsu.eventlink.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController implements CommentControllerDoc {

    private final CommentService commentService;

    @Override
    @DeleteMapping("{id}")
    @PreAuthorize("@accessManagerService.isAuthorisedAdmittedToComment(#id, authentication)")
    public void deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
    }

    @Override
    @GetMapping("/nodes/{nodeId}")
    public List<CommentDto> getCommentsByNodeId(@PathVariable Long nodeId) {
        return commentService.getCommentsByNodeId(nodeId);
    }

    @Override
    @PutMapping("/{commentId}")
    @PreAuthorize("@accessManagerService.isAuthorisedAdmittedToComment(#commentId, authentication)")
    public CommentDto editCommentMessage(@PathVariable Long commentId,
                                         @RequestBody EditableCommentDto editableCommentDto) {
        return commentService.editCommentMessage(commentId, editableCommentDto);
    }

}
