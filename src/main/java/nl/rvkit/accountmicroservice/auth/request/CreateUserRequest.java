package nl.rvkit.accountmicroservice.auth.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateUserRequest {
    private String email;

    @JsonProperty("email_verified")
    private boolean emailVerified;

    @JsonProperty("given_name")
    private String givenName;

    @JsonProperty("family_name")
    private String familyName;

    private String name;

    private String connection;

    private String password;

    public CreateUserRequest(String email, String givenName, String familyName, String password){
        this.email = email;
        this.emailVerified = true;
        this.givenName = givenName;
        this.familyName = familyName;
        this.name = givenName + " " + familyName;
        this.connection = "Username-Password-Authentication";
        this.password = password;
    }
}
