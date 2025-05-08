package by.grsu.eventlink.service;

import by.grsu.eventlink.dto.category.CategoryDto;
import by.grsu.eventlink.dto.category.TruncatedCategoryDto;
import by.grsu.eventlink.dto.common.GenericMessageDto;
import org.springframework.data.domain.Page;

public interface CategoryService {

    void deleteCategory(Long id);

    CategoryDto getCategoryById(Long categoryId);

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto getCategoryBySubCategoryId(Long subCategoryId);

    CategoryDto updateCategory(Long id, CategoryDto categoryDto);

    GenericMessageDto subscribeToCategory(String email, Long categoryId);

    GenericMessageDto unsubscribeToCategory(String email, Long categoryId);

    Page<TruncatedCategoryDto> getAllCategories(Integer pageNumber, Integer pageSize);

    Page<TruncatedCategoryDto> searchCategories(String query, Integer pageNumber, Integer pageSize);

}
