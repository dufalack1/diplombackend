package by.grsu.eventlink.service.util;

import by.grsu.eventlink.configuration.properties.MinioDataStorageProperties;
import by.grsu.eventlink.configuration.properties.enums.StorageCase;
import by.grsu.eventlink.util.storage.Extractors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StorageHelper {

    private final MinioDataStorageProperties minioDataStorageProperties;

    public String getDefaultAvatarName() {
        return minioDataStorageProperties.getDefaultImageName();
    }

    public String[] getAllowedPhotoFormats() {
        return minioDataStorageProperties.getAllowedPhotoFormats();
    }

    public String getBucketName(StorageCase storageCase) {
        return minioDataStorageProperties.getBuckets().get(storageCase).getName();
    }

    public String constructUrl(String imageName, StorageCase storageCase) {
        return Extractors.constructSignedUrl(minioDataStorageProperties.getRedirectionEndPoint(),
                getBucketName(storageCase), imageName);
    }

}
