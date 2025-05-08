package by.grsu.eventlink.service.impl;

import by.grsu.eventlink.configuration.properties.enums.StorageCase;
import by.grsu.eventlink.dto.category.CategoryDto;
import by.grsu.eventlink.dto.category.TruncatedCategoryDto;
import by.grsu.eventlink.dto.common.GenericMessageDto;
import by.grsu.eventlink.entity.Category;
import by.grsu.eventlink.entity.SubCategory;
import by.grsu.eventlink.entity.User;
import by.grsu.eventlink.exception.category.*;
import by.grsu.eventlink.exception.user.UserNotFoundException;
import by.grsu.eventlink.mapper.SubCategoryMapper;
import by.grsu.eventlink.mapper.category.CategoryMapper;
import by.grsu.eventlink.repository.CategoryRepository;
import by.grsu.eventlink.repository.SubCategoryRepository;
import by.grsu.eventlink.repository.UserRepository;
import by.grsu.eventlink.service.CategoryService;
import by.grsu.eventlink.service.util.StorageHelper;
import by.grsu.eventlink.util.collection.Collections;
import by.grsu.eventlink.util.collection.PageUtils;
import by.grsu.eventlink.util.helper.Conditional;
import liquibase.repackaged.org.apache.commons.lang3.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final StorageHelper storageHelper;

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    private final SubCategoryRepository subCategoryRepository;

    @Override
    public void deleteCategory(Long id) {
        Category deleteCandidate = categoryRepository.findCategoryById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        categoryRepository.delete(deleteCandidate);
    }

    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        Category foundedCategory = categoryRepository.findCategoryById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        foundedCategory.setImageUrl(storageHelper.constructUrl(foundedCategory.getImageUrl(), StorageCase.CATEGORY));
        CategoryDto category = CategoryMapper.mapToFullDto(foundedCategory);

//        List<SubCategory> topSubCategories = subCategoryRepository.getSubCategoriesByNodesCount(3);
        List<SubCategory> listSubCategories = foundedCategory.getSubCategories();
        category.setSubCategories(SubCategoryMapper.mapToDto(listSubCategories));
        return category;
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        validate(categoryDto);
        Conditional.fromBoolean(categoryRepository.existsByTitle(categoryDto.getTitle()))
                .ifTrueThenThrow(CategoryWithSameTitleAlreadyExistException::new);

        Category persistCategory = CategoryMapper.mapToFullDo(categoryDto);
        log.info(persistCategory.toString());
        persistCategory.setImageUrl(storageHelper.getDefaultAvatarName());
        categoryRepository.save(persistCategory);

        return CategoryMapper.mapToFullDto(persistCategory);
    }

    @Override
    public CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto) {
        Category currentCategory = categoryRepository.findCategoryById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        this.merge(currentCategory, categoryDto);

        categoryRepository.save(currentCategory);

        currentCategory.setImageUrl(storageHelper.constructUrl(currentCategory.getImageUrl(), StorageCase.CATEGORY));

        return CategoryMapper.mapToFullDto(currentCategory);
    }

    @Override
    public CategoryDto getCategoryBySubCategoryId(Long subCategoryId) {
        Long foundedCategoryId = categoryRepository.findBySubCategoryId(subCategoryId)
                .orElseThrow(() -> new CategoryNotFoundException(
                        String.format(Constants.NOT_FOUND_BY_SUB_CATEGORY_ID, subCategoryId)));

        return getCategoryById(foundedCategoryId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public GenericMessageDto subscribeToCategory(String email, Long categoryId) {
        User subscriber = userRepository.findByCredentials_Email(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        Category categoryToSubscribe = categoryRepository.findCategoryById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        Conditional.fromBoolean(userRepository.isUserLikedCategoryById(subscriber.getId(), categoryToSubscribe.getId()))
                .ifTrueThenThrow(() -> new AlreadySubscribedException(subscriber.getId(), categoryToSubscribe.getId()));

        subscriber.setLikedCategories(Collections.addAndGet(subscriber.getLikedCategories(), categoryToSubscribe));
        categoryToSubscribe.setSubscribers(Collections.addAndGet(categoryToSubscribe.getSubscribers(), subscriber));

        userRepository.save(subscriber);
        categoryRepository.save(categoryToSubscribe);

        return GenericMessageDto.builder()
                .message(String.format(Constants.FOLLOW_GENERIC_MESSAGE, subscriber.getId().toString(),
                        Constants.EMPTY, categoryId.toString()))
                .build();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public GenericMessageDto unsubscribeToCategory(String email, Long categoryId) {
        User subscriber = userRepository.findByCredentials_Email(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        Category categoryToSubscribe = categoryRepository.findCategoryById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        Conditional.fromBoolean(userRepository.isUserLikedCategoryById(subscriber.getId(), categoryToSubscribe.getId()))
                .ifFalseThenThrow(() -> new NotSubscribedException(subscriber.getId(), categoryToSubscribe.getId()));

        subscriber.setLikedCategories(Collections
                .removeAndGet(subscriber.getLikedCategories(), categoryToSubscribe));
        categoryToSubscribe.setSubscribers(Collections
                .removeAndGet(categoryToSubscribe.getSubscribers(), subscriber));

        userRepository.save(subscriber);
        categoryRepository.save(categoryToSubscribe);

        return GenericMessageDto.builder()
                .message(String.format(Constants.FOLLOW_GENERIC_MESSAGE, subscriber.getId().toString(),
                        Constants.NEGATIVE_VAL, categoryId.toString()))
                .build();
    }

    @Override
    public Page<TruncatedCategoryDto> getAllCategories(Integer pageNumber, Integer pageSize) {
        Page<Category> categories = categoryRepository.findAll(PageUtils.getPage(pageNumber, pageSize));

        Collections.ifEmptyThenThrow(categories);

        categories.forEach(category -> category.setImageUrl(
                storageHelper.constructUrl(category.getImageUrl(), StorageCase.CATEGORY)));

        return CategoryMapper.mapToDto(categories);
    }

    @Override
    public Page<TruncatedCategoryDto> searchCategories(String query, Integer pageNumber, Integer pageSize) {
        Conditional.fromBoolean(Strings.isEmpty(query))
                .ifTrueThenThrow(EmptyQueryException::new);

        Page<Category> foundedCategories = categoryRepository.
                findAllByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query,
                        PageUtils.getPage(pageNumber, pageSize));

        Collections.ifEmptyThenThrow(foundedCategories);

        foundedCategories.forEach(category -> {
            category.setImageUrl(storageHelper.constructUrl(category.getImageUrl(), StorageCase.CATEGORY));
        });

        return CategoryMapper.mapToDto(foundedCategories);
    }

    private void validate(CategoryDto categoryDto) {
        Conditional.fromBoolean(Objects.isNull(categoryDto))
                .ifTrueThenThrow(CategoryValidationFailedException::new);
        Conditional.fromBoolean(StringUtils.isEmpty(categoryDto.getTitle()))
                .ifTrueThenThrow(() -> new CategoryValidationFailedException("title"));
        Conditional.fromBoolean(StringUtils.isEmpty(categoryDto.getDescription()))
                .ifTrueThenThrow(() -> new CategoryValidationFailedException("description"));
        Conditional.fromBoolean(StringUtils.isEmpty(categoryDto.getShortDescription()))
                .ifTrueThenThrow(() -> new CategoryValidationFailedException("short description"));
    }

    private void merge(Category categoryDo, CategoryDto categoryDto) {
        if (Objects.nonNull(categoryDto.getTitle())) {
            categoryDo.setTitle(categoryDto.getTitle());
        }
        if (Objects.nonNull(categoryDto.getDescription())) {
            categoryDo.setDescription(categoryDto.getDescription());
        }
        if (Objects.nonNull(categoryDto.getShortDescription())) {
            categoryDo.setShortDescription(categoryDto.getShortDescription());
        }
    }

    private static class Constants {

        public static final String EMPTY = "";

        public static final String NEGATIVE_VAL = "un";

        public static final String FOLLOW_GENERIC_MESSAGE =
                "User with id:%s successfully %ssubscribe to category with id:%s";

        public static final String NOT_FOUND_BY_SUB_CATEGORY_ID = "Category by sub-category id:%s not found";

    }

}
