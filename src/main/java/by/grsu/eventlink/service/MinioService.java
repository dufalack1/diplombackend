package by.grsu.eventlink.service;

import by.grsu.eventlink.configuration.properties.enums.StorageCase;
import by.grsu.eventlink.dto.common.GenericMessageDto;
import org.springframework.web.multipart.MultipartFile;

public interface MinioService {

    GenericMessageDto uploadObject(MultipartFile photoToUpload, Long objectId, StorageCase storageCase);

}
