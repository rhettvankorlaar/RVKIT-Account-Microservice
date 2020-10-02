package nl.rvkit.accountmicroservice.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegistrationDTO {
    private String firstName;
    private String affix;
    private String lastName;
    private String email;
}
