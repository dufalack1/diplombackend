package by.grsu.eventlink.controller.documentation;

import by.grsu.eventlink.dto.comment.CommentDto;
import by.grsu.eventlink.dto.comment.EditableCommentDto;
import by.grsu.eventlink.dto.common.ExceptionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(
        name = "Comment controller",
        description = "Controller for managing comments"
)
public interface CommentControllerDoc {

    @Operation(
            summary = "Delete comment",
            description = "Allowed: comment author, moderation, admin"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Comment successfully deleted"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Comment by provided id was not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionDto.class
                                    )
                            )
                    )
            }
    )
    void deleteComment(Long id);

    @Operation(
            summary = "Get comments by node id",
            description = "Get comments by node id, dunno why I created it"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully pulling some comments",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = List.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Nothing was found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Node by provided id was not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionDto.class
                                    )
                            )
                    )
            }
    )
    List<CommentDto> getCommentsByNodeId(Long nodeId);

    @Operation(
            summary = "Edit comment message",
            description = "Allowed: comment author, moderator, admin"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully edited",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = CommentDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Following comment does not exist",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Comment with empty message",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionDto.class
                                    )
                            )
                    )
            }
    )
    CommentDto editCommentMessage(Long commentId, EditableCommentDto editableCommentDto);

}
