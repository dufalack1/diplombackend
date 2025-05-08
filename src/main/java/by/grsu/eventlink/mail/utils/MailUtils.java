package by.grsu.eventlink.mail.utils;

import by.grsu.eventlink.mail.enums.MailType;
import by.grsu.eventlink.util.plug.DummyUtils;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class MailUtils {

    public static final String EMAIL_FROM = "noreply@compan.era";

    private static final String MAIL_MESSAGE_FILE_PATH = "%s/%s.txt";

    private static final String RESOURCES_PATH = "src/main/resources/mail";

    private static final String MAIL_TEMPLATES_DIR = "mail/";

    @SneakyThrows
    public static String[] getMailAttributes(MailType mailType) {
        String resourcePath = MAIL_TEMPLATES_DIR + mailType.getValue() + ".txt";

        try (InputStream inputStream = MailUtils.class.getClassLoader()
                .getResourceAsStream(resourcePath)) {

            if (inputStream == null) {
                throw new RuntimeException("Mail template not found: " + resourcePath);
            }

            String message = new String(inputStream.readAllBytes());
            return message.split(DummyUtils.AMPERSAND);
        }
    }

}
