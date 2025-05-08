package by.grsu.eventlink.service.impl;

import by.grsu.eventlink.configuration.properties.enums.StorageCase;
import by.grsu.eventlink.dto.common.GenericMessageDto;
import by.grsu.eventlink.entity.Category;
import by.grsu.eventlink.entity.SubCategory;
import by.grsu.eventlink.entity.User;
import by.grsu.eventlink.exception.category.CategoryNotFoundException;
import by.grsu.eventlink.exception.common.InternalServerError;
import by.grsu.eventlink.exception.storage.FileUploadException;
import by.grsu.eventlink.exception.subcategory.SubCategoryNotFoundException;
import by.grsu.eventlink.exception.user.UserNotFoundException;
import by.grsu.eventlink.repository.CategoryRepository;
import by.grsu.eventlink.repository.SubCategoryRepository;
import by.grsu.eventlink.repository.UserRepository;
import by.grsu.eventlink.service.MinioService;
import by.grsu.eventlink.service.util.StorageHelper;
import by.grsu.eventlink.util.storage.Extractors;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioStorageServiceImpl implements MinioService {

    private final AmazonS3 amazonS3Client;

    private final StorageHelper storageHelper;

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    private final SubCategoryRepository subCategoryRepository;

    @Override
    public GenericMessageDto uploadObject(MultipartFile photoToUpload, Long objectId, StorageCase storageCase) {
        final String filename = Extractors.extractFileName(photoToUpload, objectId,
                storageHelper.getAllowedPhotoFormats());

        final String presentAvatarName = this.getPresetAvatarName(objectId, storageCase);

        if (!storageHelper.getDefaultAvatarName().equals(presentAvatarName)) {
            amazonS3Client.deleteObject(storageHelper.getBucketName(storageCase), presentAvatarName);
        }

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(photoToUpload.getSize());
        metadata.setContentType(photoToUpload.getContentType());

        try {
            amazonS3Client.putObject(storageHelper.getBucketName(storageCase), filename,
                    photoToUpload.getInputStream(), metadata);
        } catch (IOException e) {
            log.error("Error while uploading file:", e);
            throw new FileUploadException(photoToUpload.getOriginalFilename());
        }

        this.updateObjectAvatar(objectId, filename, storageCase);

        return GenericMessageDto.builder()
                .message("Picture was successfully uploaded")
                .build();
    }

    private String getPresetAvatarName(Long objectId, StorageCase storageCase) {
        switch (storageCase) {
            case USER -> {
                return userRepository.findById(objectId)
                        .orElseThrow(() -> new UserNotFoundException(objectId)).getAvatarUrl();
            }
            case CATEGORY -> {
                return categoryRepository.findCategoryById(objectId)
                        .orElseThrow(() -> new CategoryNotFoundException(objectId)).getImageUrl();
            }
            case SUB_CATEGORY -> {
                return subCategoryRepository.getById(objectId)
                        .orElseThrow(() -> new SubCategoryNotFoundException(objectId)).getImageUrl();
            }
            default -> throw new InternalServerError();
        }
    }

    private void updateObjectAvatar(Long objectId, String newAvatarName, StorageCase storageCase) {
        switch (storageCase) {
            case USER -> {
                User user = userRepository.findById(objectId)
                        .orElseThrow(() -> new UserNotFoundException(objectId));
                user.setAvatarUrl(newAvatarName);
                userRepository.save(user);
            }
            case CATEGORY -> {
                Category category = categoryRepository.findCategoryById(objectId)
                        .orElseThrow(() -> new CategoryNotFoundException(objectId));
                category.setImageUrl(newAvatarName);
                categoryRepository.save(category);
            }
            case SUB_CATEGORY -> {
                SubCategory subCategory = subCategoryRepository.getById(objectId)
                        .orElseThrow(() -> new SubCategoryNotFoundException(objectId));
                subCategory.setImageUrl(newAvatarName);
                subCategoryRepository.save(subCategory);
            }
            default -> throw new InternalServerError();
        }
    }

}
