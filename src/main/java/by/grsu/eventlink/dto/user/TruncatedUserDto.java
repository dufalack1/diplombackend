package by.grsu.eventlink.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TruncatedUserDto {

    private Long id;

    private String username;

    private String avatarUrl;

}
