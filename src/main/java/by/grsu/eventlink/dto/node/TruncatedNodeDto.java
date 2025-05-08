package by.grsu.eventlink.dto.node;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TruncatedNodeDto {

    private Long id;

    private String title;

    private String place;

    private Long ownerId;

    private String description;

    private Integer requiredPeople;

}
