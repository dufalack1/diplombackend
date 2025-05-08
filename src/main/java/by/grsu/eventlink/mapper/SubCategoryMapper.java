package by.grsu.eventlink.mapper;

import by.grsu.eventlink.dto.subcategory.SubCategoryDto;
import by.grsu.eventlink.entity.SubCategory;
import by.grsu.eventlink.mapper.category.CategoryMapper;
import by.grsu.eventlink.util.collection.PageUtils;
import liquibase.repackaged.org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SubCategoryMapper {

    public static SubCategory mapToDo(SubCategoryDto subCategory) {
        subCategory = Optional.ofNullable(subCategory).orElse(SubCategoryDto.builder().build());

        return SubCategory.builder()
                .title(subCategory.getTitle())
                .description(subCategory.getDescription())
                .shortDescription(subCategory.getShortDescription())
                .build();
    }

    public static SubCategoryDto mapToFullDto(SubCategory subCategory) {
        return SubCategoryDto.builder()
                .id(subCategory.getId())
                .title(subCategory.getTitle())
                .imageUrl(subCategory.getImageUrl())
                .description(subCategory.getDescription())
                .shortDescription(subCategory.getShortDescription())
                .category(CategoryMapper.mapToDto(subCategory.getCategoryOwner()))
                .categoryId(subCategory.getCategoryOwner().getId())
                .build();
    }

    public static Page<SubCategoryDto> mapToDto(Page<SubCategory> subCategories) {
        return new PageImpl<>(
                PageUtils.emptyIfNull(subCategories).stream()
                        .map(subCategory -> SubCategoryMapper.mapToDto((SubCategory) subCategory))
                        .collect(Collectors.toList())
        );
    }

    public static List<SubCategoryDto> mapToDto(List<SubCategory> subCategoryList) {
        return CollectionUtils.emptyIfNull(subCategoryList).stream()
                .map(SubCategoryMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public static SubCategoryDto mapToDto(SubCategory subCategory) {
        subCategory = Optional.ofNullable(subCategory)
                .orElse(SubCategory.builder().build());

        return SubCategoryDto.builder()
                .id(subCategory.getId())
                .title(subCategory.getTitle())
                .imageUrl(subCategory.getImageUrl())
                .description(subCategory.getDescription())
                .shortDescription(subCategory.getShortDescription())
                .categoryId(subCategory.getCategoryOwner().getId())
                .build();
    }

}
