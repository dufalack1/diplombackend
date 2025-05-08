package by.grsu.eventlink.util.storage;

import by.grsu.eventlink.exception.storage.WrongPhotoFormatException;
import by.grsu.eventlink.util.plug.DummyUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.Objects;

public class Extractors {

    public static final String PHOTO_FORMAT_PATTERN = "%s.%s";

    public static final String LINK_FORMAT_PATTERN = "%s/%s/%s";

    public static String extractFileName(MultipartFile multipartFile, Long id,
                                         @NotEmpty String[] allowedPhotoFormat) {
        String format = extractPictureFormat(multipartFile.getOriginalFilename());

        if (Arrays.stream(allowedPhotoFormat)
                .noneMatch(allowedFormat -> allowedFormat.equals(format))) {
            throw new WrongPhotoFormatException();
        }

        return String.format(PHOTO_FORMAT_PATTERN, id.toString(), format);
    }

    public static String extractPictureFormat(String filename) {
        String[] temp = Objects.requireNonNull(filename).split("\\.");

        if (temp.length == 0) {
            throw new WrongPhotoFormatException();
        }

        return temp[temp.length - 1];
    }

    public static String constructSignedUrl(String redirectionEndPoint, String bucketName, String source) {
        return String.format(LINK_FORMAT_PATTERN, redirectionEndPoint,
                bucketName, source);
    }

    public static String extractCityFromPlace(String place) {
        final String[] placeSplit = place.split(DummyUtils.COMMA);

        if (place.length() >= 1) {
            return placeSplit[DummyUtils.ZERO];
        } else {
            return Strings.EMPTY;
        }
    }

}
