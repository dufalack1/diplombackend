package by.grsu.eventlink.mail.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MailType {

    GREETINGS("greetings"),

    INVITATION("invitation"),

    NODE_INVITATION("node_inv");

    private final String value;

}
