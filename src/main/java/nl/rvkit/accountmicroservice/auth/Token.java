package nl.rvkit.accountmicroservice.auth;

import lombok.Getter;
import lombok.Setter;
import nl.rvkit.accountmicroservice.util.Util;

import java.util.Date;
import java.util.List;
@Getter
@Setter
public class Token {
    private Date acquiredAt;

    private String accessToken;

    private String tokenType;

    private List<String> scopes;

    private int expiresIn;

    public Date getExpiresAt() {
        return Util.addMinutesToDate(this.expiresIn, this.acquiredAt);
    }
}
