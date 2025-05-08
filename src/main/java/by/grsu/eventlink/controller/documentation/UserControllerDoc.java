package by.grsu.eventlink.controller.documentation;

import by.grsu.eventlink.dto.auth.AuthenticationResponseDto;
import by.grsu.eventlink.dto.common.ExceptionDto;
import by.grsu.eventlink.dto.common.GenericMessageDto;
import by.grsu.eventlink.dto.user.UserDto;
import by.grsu.eventlink.dto.user.UserRegistrationRequestDto;
import by.grsu.eventlink.entity.enums.RoleHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;

import java.util.List;

@Tag(name = "User controller", description = "Controller for managing user entity")
public interface UserControllerDoc {

    @Operation(
            summary = "Get user by it's id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Someone successfully was founded.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Unfortunately no one was found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class)
                            )
                    )
            }
    )
    UserDto getById(Long id);

    @Operation(
            summary = "Delete user by id",
            description = "Deleting user by ID. No one should have access to this method except account owner or admin"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User successfully deleted",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GenericMessageDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User with provided ID was not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class)
                            )
                    )
            }
    )
    GenericMessageDto delete(Long id);

    @Operation(
            summary = "Update user info",
            description = "Updating user info."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful updating.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User with provided id was not found.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class)
                            )
                    )
            }
    )
    UserDto update(Long id, UserDto userDto);

    @Operation(
            summary = "Grant role",
            description = "Admin allowed only. BLOCKED to ban someone"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Role successfully granted"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Unfortunately user not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Unfortunately role not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "User already have following role",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "You cannot grant role to yourself",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class)
                            )
                    )
            }
    )
    void grantRole(Long userId, RoleHelper role, Authentication authentication);

    @Operation(
            summary = "Remove role",
            description = "Admin allowed only. BLOCKED to unban someone"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Role successfully removed"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Unfortunately user not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Unfortunately role not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "User already haven't following role",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "You cannot remove role from yourself",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class)
                            )
                    )
            }
    )
    void removeRole(Long userId, RoleHelper role, Authentication authentication);

    @Operation(
            summary = "Get users by displayed name match",
            description = "This method will be used when user searching for someone by displayed name"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Someone successfully was founded. Content is LIST of users.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Unfortunately no one was found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class)
                            )
                    )
            }
    )
    List<UserDto> getUsersByDisplayedNameMatch(String query);

    @Operation(
            summary = "User registration method",
            description = "The endpoint for user registration. Invitation required first."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User successfully registered in the system. Greetings email was sent.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Invitation for current email was not found.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Invitation code miss match.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Credentials with same email already exist.",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ExceptionDto.class)
                                    )
                            }
                    )
            }
    )
    AuthenticationResponseDto registration(UserRegistrationRequestDto registrationRequestDto);

}
