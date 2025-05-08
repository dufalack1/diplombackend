package by.grsu.eventlink.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TruncatedCategoryDto {

    private Long id;

    private String title;

    private String imageUrl;

    private String shortDescription;

}
