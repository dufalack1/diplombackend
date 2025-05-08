package by.grsu.eventlink.service;

import by.grsu.eventlink.dto.subcategory.SubCategoryDto;
import org.springframework.data.domain.Page;

public interface SubCategoryService {

    void deleteSubCategoryById(Long subCategoryId);

    SubCategoryDto getSubCategoryById(Long subCategoryId);

    SubCategoryDto createSubCategory(SubCategoryDto subCategoryDto);

    Page<SubCategoryDto> getSubCategoriesByCategoryId(Long categoryId, Integer pageNumber, Integer pageSize);

    Page<SubCategoryDto> getSubCategoriesByAnyFieldMatch(String query, Integer pageNumber, Integer pageSize);

}
