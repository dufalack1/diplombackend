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
 * Node invitation Postman class
 * Send method attributes:
 *  1. [0] - username
 *  2. [1] - node title
 *  3. [2] - node description
 *  4. [3] - node age limit
 * */
@Component("nodeInvitationPostman")
public class NodeInvitationPostmanImpl implements Postman {

    private final String title;

    private final String message;

    private final JavaMailSender mailSender;

    @Getter
    private final MailType mailType = MailType.NODE_INVITATION;

    public NodeInvitationPostmanImpl(JavaMailSender mailSender) {
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

        simpleMailMessage.setText(String.format(message, attributes.get(0), attributes.get(1),
                attributes.get(2), attributes.get(3)));

        mailSender.send(simpleMailMessage);
    }
}
