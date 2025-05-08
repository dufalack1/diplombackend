package by.grsu.eventlink.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequestDto {

    @Email
    @NotBlank
    private String email;

    @NotNull
    private Date birthDate;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String phoneNumber;

    @NotBlank
    private String confirmationCode;

}
