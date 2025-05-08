package by.grsu.eventlink.mail.impl;

import by.grsu.eventlink.mail.Postman;
import by.grsu.eventlink.mail.enums.MailType;
import by.grsu.eventlink.mail.utils.MailUtils;
import by.grsu.eventlink.util.plug.DummyUtils;
import lombok.Getter;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Greetings Postman class
 * Send method attributes:
 *  1. [0] - person nickname
 * */
@Component("greetingsPostman")
public class GreetingsPostmanImpl implements Postman {

    private final String title;

    private final String message;

    private final JavaMailSender mailSender;

    @Getter
    private final MailType mailType = MailType.GREETINGS;

    public GreetingsPostmanImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;

        final String[] attributes = MailUtils.getMailAttributes(mailType);

        this.title = attributes[DummyUtils.ZERO];
        this.message = attributes[DummyUtils.ONE];
    }

    @Override
    public void send(String email, List<String> attributes) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setSubject(title);
        simpleMailMessage.setFrom(MailUtils.EMAIL_FROM);
        simpleMailMessage.setTo(email);

        simpleMailMessage.setText(String.format(message, attributes.get(0)));

        mailSender.send(simpleMailMessage);
    }

}
