package by.grsu.eventlink.dto.node;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NodeSearchingAttributesDto {

    private Long subCategoryId;

    private String titleContains;

    private String descriptionContains;


}
