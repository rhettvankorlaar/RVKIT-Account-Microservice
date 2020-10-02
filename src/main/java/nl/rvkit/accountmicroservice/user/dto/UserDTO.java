package nl.rvkit.accountmicroservice.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String affix;
    private String lastName;
    private String Auth0UserId;
}
