package nl.rvkit.accountmicroservice.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Getter
@Setter
@Component
@ConfigurationProperties("auth0")
public class AuthSettings {
    private String apiAudience;

    private String issuer;

    private String clientId;

    private String clientSecret;
}
