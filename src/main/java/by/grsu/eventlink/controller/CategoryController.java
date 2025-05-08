package by.grsu.eventlink.controller;

import by.grsu.eventlink.configuration.properties.enums.StorageCase;
import by.grsu.eventlink.controller.documentation.CategoryControllerDoc;
import by.grsu.eventlink.dto.category.CategoryDto;
import by.grsu.eventlink.dto.category.TruncatedCategoryDto;
import by.grsu.eventlink.dto.common.GenericMessageDto;
import by.grsu.eventlink.service.CategoryService;
import by.grsu.eventlink.service.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController implements CategoryControllerDoc {

    private final MinioService minioService;

    private final CategoryService categoryService;

    @Override
    @DeleteMapping("{id}")
    @PostAuthorize("@accessManagerService.isAuthorisedAdmin(authentication)")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }

    @Override
    @GetMapping("{id}")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @Override
    @PostMapping("/")
    @PostAuthorize("@accessManagerService.isAuthorisedAdmin(authentication)")
    public CategoryDto createCategory(@RequestBody @Validated CategoryDto categoryDto) {
        return categoryService.createCategory(categoryDto);
    }

    @Override
    @GetMapping("all")
    public Page<TruncatedCategoryDto> getAllCategories(@RequestParam("size") Integer pageSize,
                                                       @RequestParam("page") Integer pageNumber) {
        return categoryService.getAllCategories(pageNumber, pageSize);
    }

    @Override
    @PutMapping("{id}")
    @PostAuthorize("@accessManagerService.isAuthorisedAdmin(authentication)")
    public CategoryDto updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
        return categoryService.updateCategory(id, categoryDto);
    }

    @PutMapping("{id}/avatar")
    @PostAuthorize("@accessManagerService.isAuthorisedAdmin(authentication)")
    public GenericMessageDto uploadAvatar(@PathVariable Long id, @RequestParam("picture") MultipartFile picture) {
        return minioService.uploadObject(picture, id, StorageCase.CATEGORY);
    }

    @Override
    @PutMapping("{categoryId}/subscribe")
    public GenericMessageDto subscribeToCategory(@PathVariable Long categoryId, Authentication authentication) {
        return categoryService.subscribeToCategory(authentication.getName(), categoryId);
    }

    @Override
    @DeleteMapping("{categoryId}/unsubscribe")
    public GenericMessageDto unsubscribeToCategory(@PathVariable Long categoryId, Authentication authentication) {
        return categoryService.unsubscribeToCategory(authentication.getName(), categoryId);
    }

    @Override
    @GetMapping
    public CategoryDto getCategoryBySubCategoryId(@RequestParam("bsc") Long subCategoryId) {
        return categoryService.getCategoryBySubCategoryId(subCategoryId);
    }

    @Override
    @GetMapping("/search")
    public Page<TruncatedCategoryDto> searchCategories(@RequestParam("q") String query,
                                                       @RequestParam("page") Integer pageNumber,
                                                       @RequestParam("size") Integer pageSize) {
        return categoryService.searchCategories(query, pageNumber, pageSize);
    }
}
