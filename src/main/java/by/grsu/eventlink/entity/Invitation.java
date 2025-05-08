package by.grsu.eventlink.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "invitations")
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "generated_code")
    private String generatedCode;

    @Column(name = "expiration_time")
    private Date expirationTime;

    @Column(name = "is_active")
    private Boolean isActive;

}
