package by.grsu.eventlink.dto.subcategory;

import by.grsu.eventlink.dto.category.TruncatedCategoryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryDto {

    private Long id;

    @NotBlank
    private String title;

    private String imageUrl;

    @NotBlank
    private String description;

    @NotBlank
    private String shortDescription;

    private TruncatedCategoryDto category;

    @NotBlank
    private Long categoryId;

}
