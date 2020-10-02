package nl.rvkit.accountmicroservice.auth.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;
@Getter
@Setter
public class UserInfoResponse {
    @JsonProperty("sub")
    private String sub;

    @JsonProperty("given_name")
    private String givenName;

    @JsonProperty("family_name")
    private String familyName;

    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("name")
    private String name;

    @JsonProperty("picture")
    private String pictureUrl;

    @JsonProperty("locale")
    private String locale;

    @JsonProperty("updated_at")
    private Date updatedAt;

    @JsonProperty("email")
    private String email;

    @JsonProperty("email_verified")
    private boolean emailVerified;

    @JsonProperty("app_metadata")
    private Map<String, Object> appMetadata;
}
