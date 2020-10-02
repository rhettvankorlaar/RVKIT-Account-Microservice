package nl.rvkit.accountmicroservice.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.rvkit.accountmicroservice.auth.response.UserInfoResponse;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class AuthUser {
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

    public AuthUser(UserInfoResponse userInfoResponse) {
        this.sub = userInfoResponse.getSub();
        this.givenName = userInfoResponse.getGivenName();
        this.familyName = userInfoResponse.getFamilyName();
        this.nickname = userInfoResponse.getNickname();
        this.name = userInfoResponse.getName();
        this.pictureUrl = userInfoResponse.getPictureUrl();
        this.locale = userInfoResponse.getLocale();
        this.updatedAt = userInfoResponse.getUpdatedAt();
        this.email = userInfoResponse.getEmail();
        this.emailVerified = userInfoResponse.isEmailVerified();
        this.appMetadata = userInfoResponse.getAppMetadata();
    }
}
