package by.grsu.eventlink.mail;

import by.grsu.eventlink.mail.enums.MailType;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface Postman {

    MailType getMailType();

    @Async
    @Retryable(retryFor = Exception.class)
    void send(String email, List<String> attributes);

}
