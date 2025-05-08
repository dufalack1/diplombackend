package by.grsu.eventlink.mapper.category;

import by.grsu.eventlink.dto.category.CategoryDto;
import by.grsu.eventlink.dto.category.TruncatedCategoryDto;
import by.grsu.eventlink.entity.Category;
import by.grsu.eventlink.util.collection.PageUtils;
import liquibase.repackaged.org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CategoryMapper {

    public static Category mapToFullDo(CategoryDto categoryDto) {
        return mapToFullDo(null, categoryDto);
    }

    public static Category mapToFullDo(Long id, CategoryDto categoryDto) {
        categoryDto = Optional.ofNullable(categoryDto).orElse(CategoryDto.builder().build());

        return Category.builder()
                .id(id)
                .title(categoryDto.getTitle())
                .description(categoryDto.getDescription())
                .shortDescription(categoryDto.getShortDescription())
                .build();
    }

    public static CategoryDto mapToFullDto(Category category) {
        category = Optional.ofNullable(category).orElse(Category.builder().build());

        return CategoryDto.builder()
                .id(category.getId())
                .title(category.getTitle())
                .imageUrl(category.getImageUrl())
                .description(category.getDescription())
                .shortDescription(category.getShortDescription())
                .build();
    }

    public static Page<TruncatedCategoryDto> mapToDto(Page<Category> categories) {
        return new PageImpl<>(
                PageUtils.emptyIfNull(categories).stream()
                        .map(category -> CategoryMapper.mapToDto((Category) category))
                        .collect(Collectors.toList())
        );
    }

    public static List<TruncatedCategoryDto> mapToDto(List<Category> categories) {
        return CollectionUtils.emptyIfNull(categories).stream()
                .map(CategoryMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public static TruncatedCategoryDto mapToDto(Category category) {
        category = Optional.ofNullable(category).orElse(Category.builder().build());

        return TruncatedCategoryDto.builder()
                .id(category.getId())
                .title(category.getTitle())
                .imageUrl(category.getImageUrl())
                .shortDescription(category.getShortDescription())
                .build();
    }

}
