package by.grsu.eventlink.controller.documentation;

import by.grsu.eventlink.dto.common.GenericMessageDto;
import by.grsu.eventlink.dto.invitation.InvitationRequestDto;
import by.grsu.eventlink.exception.common.NotFoundException;
import by.grsu.eventlink.exception.user.UserAlreadyExistException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Invitation controller", description = "Invitation sending manager")
public interface InvitationControllerDoc {

    @Operation(
            summary = "Invitation resending",
            description = "Method is used for resending already existing invitations"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Invitation was resend",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = GenericMessageDto.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Invitation was never sent to the user",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = NotFoundException.class)
                                    )
                            }
                    )
            }
    )
    GenericMessageDto mailResend(InvitationRequestDto invitationRequestDto);

    @Operation(
            summary = "Invitation request",
            description = "Invitation request method"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Invitation email with generated six digits code was sent",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = GenericMessageDto.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Credentials with same email or active invitation already exist",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = UserAlreadyExistException.class)
                                    )
                            }
                    )
            }
    )
    GenericMessageDto invitationRequest(InvitationRequestDto invitationRequestDto);

}
