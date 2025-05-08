package by.grsu.eventlink.controller;

import by.grsu.eventlink.controller.documentation.SubCategoryControllerDoc;
import by.grsu.eventlink.dto.subcategory.SubCategoryDto;
import by.grsu.eventlink.service.SubCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sub-categories")
public class SubCategoryController implements SubCategoryControllerDoc {

    private final SubCategoryService subCategoryService;


    @Override
    @DeleteMapping("{subCategoryId}")
    @PreAuthorize("@accessManagerService.isAuthorisedModerator(authentication)")
    public void deleteSubCategoryById(@PathVariable Long subCategoryId) {
        subCategoryService.deleteSubCategoryById(subCategoryId);
    }

    @Override
    @GetMapping("/{subCategoryId}")
    public SubCategoryDto getSubCategoryById(@PathVariable Long subCategoryId) {
        return subCategoryService.getSubCategoryById(subCategoryId);
    }

    @Override
    @PostMapping
    @PreAuthorize("@accessManagerService.isAuthorisedModerator(authentication)")
    public SubCategoryDto createSubCategory(@RequestBody SubCategoryDto subCategoryDto) {
        return subCategoryService.createSubCategory(subCategoryDto);
    }

    @Override
    @GetMapping("/categories/{categoryId}")
    public Page<SubCategoryDto> getSubCategoriesByCategoryId(@PathVariable Long categoryId,
                                                             @RequestParam("page") Integer pageNumber,
                                                             @RequestParam("size") Integer pageSize) {
        return subCategoryService.getSubCategoriesByCategoryId(categoryId, pageNumber, pageSize);
    }

    @Override
    @GetMapping("/search")
    public Page<SubCategoryDto> getSubCategoriesByAnyFieldMatch(@RequestParam("q") String query,
                                                                @RequestParam("page") Integer pageNumber,
                                                                @RequestParam("size") Integer pageSize) {
        return subCategoryService.getSubCategoriesByAnyFieldMatch(query, pageNumber, pageSize);
    }
}
