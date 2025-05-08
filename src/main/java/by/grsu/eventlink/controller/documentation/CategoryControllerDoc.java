package by.grsu.eventlink.controller.documentation;

import by.grsu.eventlink.dto.category.CategoryDto;
import by.grsu.eventlink.dto.category.TruncatedCategoryDto;
import by.grsu.eventlink.dto.common.ExceptionDto;
import by.grsu.eventlink.dto.common.GenericMessageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

@Tag(name = "Category controller", description = "Controller for managing category entity")
public interface CategoryControllerDoc {

    @Operation(
            summary = "Delete category by id",
            description = "SubCategories will be detached"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Category was successfully deleted",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CategoryDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Category with provided id was not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class)
                            )
                    )
            }
    )
    void deleteCategory(Long id);

    @Operation(
            summary = "Get category by id",
            description = "Nothing special :("
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful get by id",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CategoryDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Category with provided id was not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class)
                            )
                    )
            }
    )
    CategoryDto getCategoryById(Long id);

    @Operation(
            summary = "Create category (admin only)",
            description = "Just creation endpoint, admin permitted only"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Category was successfully created, default avatar assigned",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CategoryDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Validation failed. Some of data haven't provided.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Category with same title already exist",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class)
                            )
                    )
            }
    )
    CategoryDto createCategory(CategoryDto categoryDto);

    @Operation(
            summary = "Get category by sub category ID",
            description = "Everything as in the title ;)"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Category successfully found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CategoryDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Category by following sub category id was not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class)
                            )
                    )
            }
    )
    CategoryDto getCategoryBySubCategoryId(Long subCategoryId);

    @Operation(
            summary = "Update category (admin permitted only)",
            description = "Category update endpoint"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Category was successfully updated",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CategoryDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Category by provided id was not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class)
                            )
                    )
            }
    )
    CategoryDto updateCategory(Long id, CategoryDto categoryDto);

    @Operation(
            summary = "Get all categories",
            description = "Get all pageable"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Something successful founded",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TruncatedCategoryDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Nothing was found by following query",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class)
                            )
                    )
            }
    )
    Page<TruncatedCategoryDto> getAllCategories(Integer pageSize, Integer pageNumber);

    @Operation(
            summary = "Like category endpoint",
            description = "User can like category to have it in his profile"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User successfully like category",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GenericMessageDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Authorised user was not found somehow",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Category by provided id was not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Authorised user already subscribed to following category",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class)
                            )
                    )
            }
    )
    GenericMessageDto subscribeToCategory(Long categoryId, Authentication authentication);

    @Operation(
            summary = "Unsubscribe category endpoint",
            description = "User can unlike category"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User successfully unsubscribe the category",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GenericMessageDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Authorised user was not found somehow",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Category by provided id was not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Authorised user haven't subscribed to following category",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class)
                            )
                    )
            }
    )
    GenericMessageDto unsubscribeToCategory(Long categoryId, Authentication authentication);

    @Operation(
            summary = "Search category by query",
            description = "Search category by title/description/short description"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Something successful founded",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TruncatedCategoryDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Nothing was found by following query",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDto.class)
                            )
                    )
            }
    )
    Page<TruncatedCategoryDto> searchCategories(String query, Integer pageNumber, Integer pageSize);

}
