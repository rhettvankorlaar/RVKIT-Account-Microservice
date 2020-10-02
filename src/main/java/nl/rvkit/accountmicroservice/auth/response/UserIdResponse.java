package nl.rvkit.accountmicroservice.auth.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserIdResponse {
    @JsonProperty("user_id")
    private String userId;
}
