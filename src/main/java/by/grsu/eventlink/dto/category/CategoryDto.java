package by.grsu.eventlink.dto.category;

import by.grsu.eventlink.dto.subcategory.SubCategoryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private Long id;

    @NotBlank
    private String title;

    private String imageUrl;

    @NotBlank
    private String description;

    @NotBlank
    private String shortDescription;

    private List<SubCategoryDto> subCategories;

}
