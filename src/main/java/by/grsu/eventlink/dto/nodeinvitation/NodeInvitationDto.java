package by.grsu.eventlink.dto.nodeinvitation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NodeInvitationDto {

    private Long id;

    private Long nodeId;

    private Long invitedId;

    private Boolean isInvited;

}
