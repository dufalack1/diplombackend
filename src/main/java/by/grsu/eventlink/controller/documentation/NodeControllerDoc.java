package by.grsu.eventlink.controller.documentation;

import by.grsu.eventlink.dto.comment.CommentDto;
import by.grsu.eventlink.dto.common.ExceptionDto;
import by.grsu.eventlink.dto.common.GenericMessageDto;
import by.grsu.eventlink.dto.node.GenericBufferDto;
import by.grsu.eventlink.dto.node.NodeDto;
import by.grsu.eventlink.dto.node.TruncatedNodeDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

@Tag(name = "Node controller", description = "Controller for managing nodes")
public interface NodeControllerDoc {

    @Operation(
            summary = "Get node by ID.",
            description = "Get full node by ID"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Node by provided ID was successfully founded.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = NodeDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Node by provided ID was not found.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionDto.class
                                    )
                            )
                    )
            }
    )
    NodeDto getById(Long id, Authentication authentication);

    @Operation(
            summary = "Delete node by ID.",
            description = "Delete node by ID"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Node by provided ID was successfully deleted.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = GenericMessageDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Node by provided ID was not found.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionDto.class
                                    )
                            )
                    )
            }
    )
    GenericMessageDto delete(Long id);

    @Operation(
            summary = "Leave some comment",
            description = "Just leave some comment, nothing special"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Comment successfully leaved",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = CommentDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Node where you are trying to comment someone was not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Author was not found (by authorId).",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Empty message",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Anti-spam system",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionDto.class
                                    )
                            )
                    )
            }
    )
    CommentDto leaveComment(CommentDto commentDto);

    @Operation(
            summary = "Create node.",
            description = "Create node (subCategoryId and ownerId required). Expiration date = NOW + 30days."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Node was successfully created.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = GenericMessageDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Sub category was not found (by subCategoryId).",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Owner was not found (by ownerId).",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionDto.class
                                    )
                            )
                    )
            }
    )
    GenericMessageDto registerEvent(NodeDto nodeDto);

    @Operation(
            summary = "Node invitation method",
            description = "Node invitation method. You're need to provide node id and user id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User was successfully invited",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = GenericMessageDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User you are trying to invite was not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Node where you are trying to invite someone was not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Node required people was overflow",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionDto.class
                                    )
                            )
                    )
            }
    )
    GenericMessageDto invitePeople(Long id, GenericBufferDto bufferDto);

    @Operation(
            summary = "Node join request",
            description = "Method for requesting the node joining"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Join request was successfully sent",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = GenericMessageDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User that trying to join was not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Node where you are trying to join was not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionDto.class
                                    )
                            )
                    )
            }
    )
    GenericMessageDto requestNodeJoin(Long id, Authentication authentication);

    @Operation(
            summary = "Reject someone invitation",
            description = "Method for rejecting the node invitation"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Invitation was successfully declined",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = GenericMessageDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User that trying to decline the invitation was not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Node where you are trying to decline the invitation was not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "You are not invited to the following node",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionDto.class
                                    )
                            )
                    )
            }
    )
    GenericMessageDto cancelInvitation(Long id, GenericBufferDto bufferDto);

    @Operation(
            summary = "Accept someone invitation",
            description = "Method for accepting the node invitation"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Invitation was successfully accepted",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = GenericMessageDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User that trying to accept the invitation was not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Node where you are trying to accept the invitation was not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "You are not invited to the following node",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionDto.class
                                    )
                            )
                    )
            }
    )
    GenericMessageDto acceptJoinInvitation(Long id, Authentication authentication);

    @Operation(
            summary = "Force uninvite person from the event.",
            description = "Uninviting someone from the event --force :))"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Person successfully ejected from the event",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = GenericMessageDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User you are trying to remove was not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Node where you are trying to remove someone was not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionDto.class
                                    )
                            )
                    )
            }
    )
    GenericMessageDto removeJoinedPeople(Long id, GenericBufferDto genericBufferDto);

    @Operation(
            summary = "Get nodes",
            description = "Get nodes by title or subCategory match"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Nodes was successfully founded",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = Page.class
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
                    )
            }
    )
    Page<TruncatedNodeDto> getTruncatedNodesByAttributes(Integer page, Integer size,
                                                         Long subCategoryId, String titleContains,
                                                         String descriptionContains);

}
