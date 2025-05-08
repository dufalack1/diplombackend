package by.grsu.eventlink.dto.user;

import by.grsu.eventlink.dto.node.TruncatedNodeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserNodesDto {

    private List<TruncatedNodeDto> owned;

    private List<TruncatedNodeDto> joined;

}
