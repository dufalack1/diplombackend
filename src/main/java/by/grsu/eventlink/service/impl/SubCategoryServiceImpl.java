package by.grsu.eventlink.service.impl;

import by.grsu.eventlink.configuration.properties.enums.StorageCase;
import by.grsu.eventlink.dto.subcategory.SubCategoryDto;
import by.grsu.eventlink.entity.Category;
import by.grsu.eventlink.entity.SubCategory;
import by.grsu.eventlink.exception.category.CategoryNotFoundException;
import by.grsu.eventlink.exception.common.ValidationException;
import by.grsu.eventlink.exception.subcategory.SubCategoryAlreadyExist;
import by.grsu.eventlink.exception.subcategory.SubCategoryNotFoundException;
import by.grsu.eventlink.mapper.SubCategoryMapper;
import by.grsu.eventlink.repository.CategoryRepository;
import by.grsu.eventlink.repository.NodeRepository;
import by.grsu.eventlink.repository.SubCategoryRepository;
import by.grsu.eventlink.service.SubCategoryService;
import by.grsu.eventlink.service.util.StorageHelper;
import by.grsu.eventlink.util.collection.Collections;
import by.grsu.eventlink.util.collection.PageUtils;
import by.grsu.eventlink.util.helper.Conditional;
import liquibase.repackaged.org.apache.commons.lang3.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubCategoryServiceImpl implements SubCategoryService {

    private final StorageHelper storageHelper;

    private final CategoryRepository categoryRepository;

    private final SubCategoryRepository subCategoryRepository;
    private final NodeRepository nodeRepository;

    @Override
    public void deleteSubCategoryById(Long subCategoryId) {
        Conditional.fromBoolean(subCategoryRepository.existsById(subCategoryId))
                .ifFalseThenThrow(() -> new SubCategoryNotFoundException(subCategoryId));

        subCategoryRepository.deleteById(subCategoryId);

    }

    @Override
    public SubCategoryDto getSubCategoryById(Long subCategoryId) {
        SubCategory subCategory = subCategoryRepository.getById(subCategoryId)
                .orElseThrow(() -> new SubCategoryNotFoundException(subCategoryId));

        subCategory.setImageUrl(storageHelper.constructUrl(subCategory.getImageUrl(), StorageCase.SUB_CATEGORY));

        return SubCategoryMapper.mapToFullDto(subCategory);
    }

    @Override
    public SubCategoryDto createSubCategory(SubCategoryDto subCategoryDto) {
        validate(subCategoryDto);

        Conditional.fromBoolean(subCategoryRepository.existsByTitle(subCategoryDto.getTitle()))
                .ifTrueThenThrow(SubCategoryAlreadyExist::new);

        SubCategory subCategory = SubCategoryMapper.mapToDo(subCategoryDto);
        Category pinnedCategory = categoryRepository.findCategoryById(subCategoryDto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(subCategoryDto.getCategoryId()));

        subCategory.setCategoryOwner(pinnedCategory);
        subCategory.setImageUrl(storageHelper.getDefaultAvatarName());

        subCategoryRepository.save(subCategory);

        subCategory.setImageUrl(
                storageHelper.constructUrl(subCategory.getImageUrl(), StorageCase.SUB_CATEGORY)
        );
        subCategory.getCategoryOwner().setImageUrl(
                storageHelper.constructUrl(subCategory.getCategoryOwner().getImageUrl(), StorageCase.CATEGORY)
        );

        return SubCategoryMapper.mapToFullDto(subCategory);
    }

    @Override
    public Page<SubCategoryDto> getSubCategoriesByCategoryId(Long categoryId, Integer pageNumber, Integer pageSize) {
        Page<SubCategory> subCategories = subCategoryRepository.getByCategoryId(categoryId,
                PageUtils.getPage(pageNumber, pageSize));

        Collections.ifEmptyThenThrow(subCategories);

        subCategories.forEach(subCategory -> subCategory.setImageUrl(
                storageHelper.constructUrl(subCategory.getImageUrl(), StorageCase.SUB_CATEGORY)));

        return SubCategoryMapper.mapToDto(subCategories);
    }

    @Override
    public Page<SubCategoryDto> getSubCategoriesByAnyFieldMatch(String query, Integer pageNumber, Integer pageSize) {
        Page<SubCategory> subCategories = subCategoryRepository.getSubCategoriesAnyFieldMatch(query,
                PageUtils.getPage(pageNumber, pageSize));

        Collections.ifEmptyThenThrow(subCategories);

        subCategories.forEach(subCategory -> subCategory.setImageUrl(
                storageHelper.constructUrl(subCategory.getImageUrl(), StorageCase.SUB_CATEGORY)));

        return SubCategoryMapper.mapToDto(subCategories);
    }

    private void validate(SubCategoryDto subCategory) {
        Conditional.fromBoolean(StringUtils.isEmpty(subCategory.getTitle()))
                .ifTrueThenThrow(() -> new ValidationException("title"));
        Conditional.fromBoolean(StringUtils.isEmpty(subCategory.getDescription()))
                .ifTrueThenThrow(() -> new ValidationException("description"));
        Conditional.fromBoolean(StringUtils.isEmpty(subCategory.getShortDescription()))
                .ifTrueThenThrow(() -> new ValidationException("short description"));
        Optional.ofNullable(subCategory.getCategoryId())
                .orElseThrow(CategoryNotFoundException::new);
    }

}
