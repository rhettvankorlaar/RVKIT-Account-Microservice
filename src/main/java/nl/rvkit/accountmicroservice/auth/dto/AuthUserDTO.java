package nl.rvkit.accountmicroservice.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Map;
@Getter
@Setter
@NoArgsConstructor
public class AuthUserDTO {
    private String sub;

    private String givenName;

    private String familyName;

    private String nickname;

    private String name;

    private String pictureUrl;

    private String locale;

    private Date updatedAt;

    private String email;

    private boolean emailVerified;

    private Map<String, Object> appMetadata;
}
