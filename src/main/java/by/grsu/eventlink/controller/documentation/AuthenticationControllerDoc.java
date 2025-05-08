package by.grsu.eventlink.controller.documentation;

import by.grsu.eventlink.dto.auth.AuthenticationRequestDto;
import by.grsu.eventlink.dto.auth.AuthenticationResponseDto;
import by.grsu.eventlink.dto.common.ExceptionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Authentication controller", description = "Supports user authentication")
public interface AuthenticationControllerDoc {

    @Operation(summary = "Authentication", description = "Authentication under the e-mail and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful user authentication",
                    content = {
                        @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = AuthenticationResponseDto.class)
                        )
                    }),
            @ApiResponse(responseCode = "400",
                    description = "Authentication failed for some reason (most trivial: wrong email or password)",
                    content = {
                        @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ExceptionDto.class)
                        )
                    })
    })
    AuthenticationResponseDto authenticate(AuthenticationRequestDto authenticationRequest);

}
