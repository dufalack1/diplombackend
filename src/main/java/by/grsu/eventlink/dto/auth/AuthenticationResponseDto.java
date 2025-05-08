package by.grsu.eventlink.dto.auth;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponseDto {

    private Long id;

    private String token;

    private String username;

    private String avatarUrl;

    private List<String> roles;

}
