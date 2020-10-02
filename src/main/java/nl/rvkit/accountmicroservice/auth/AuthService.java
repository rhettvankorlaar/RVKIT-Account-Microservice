package nl.rvkit.accountmicroservice.auth;

import nl.rvkit.accountmicroservice.auth.dto.AuthUserDTO;
import nl.rvkit.accountmicroservice.auth.request.CreateUserRequest;
import nl.rvkit.accountmicroservice.auth.response.CreateUserResponse;
import nl.rvkit.accountmicroservice.auth.response.GetTokenResponse;
import nl.rvkit.accountmicroservice.auth.response.UserIdResponse;
import nl.rvkit.accountmicroservice.auth.response.UserInfoResponse;
import nl.rvkit.accountmicroservice.util.Mapper;
import nl.rvkit.accountmicroservice.util.Util;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class AuthService implements IAuthService {
    private final RestTemplate restTemplate;
    private final AuthSettings authSettings;
    private Token token;

    public AuthService(RestTemplate restTemplate, AuthSettings authSettings) {
        this.restTemplate = restTemplate;
        this.authSettings = authSettings;
    }

    @Override
    public AuthUserDTO getAuthUser() {
        String token = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(token);
        HttpEntity<UserInfoResponse> userInfoRequestHttpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<UserInfoResponse> userInfoRequestResponseEntity = restTemplate.exchange(authSettings.getIssuer() + "userinfo", HttpMethod.GET, userInfoRequestHttpEntity, UserInfoResponse.class);
        UserInfoResponse userInfoResponse = userInfoRequestResponseEntity.getBody();
        if (userInfoResponse == null) {
            throw new EntityNotFoundException("User could not identify itself");
        }
        AuthUser authUser = new AuthUser(userInfoResponse);
        return Mapper.map(authUser, AuthUserDTO.class);
    }

    @Override
    public String createAuthUser(AuthUserDTO authUser) {
        //TODO add email check on auth0 side checkIfEmailAddressAlreadyExists()
        CreateUserRequest createUserRequest = new CreateUserRequest(authUser.getEmail(), authUser.getGivenName(), authUser.getFamilyName(), Util.generateTemporaryPassword());
        ResponseEntity<CreateUserResponse> response = doRequest("/api/v2/users", HttpMethod.POST, createUserRequest, CreateUserResponse.class);
        assert response.getBody() != null;
        return response.getBody().getUserId();
    }

    @Override
    public String requestPasswordResetLink(String auth0UserId) {
        return null;
    }

    @Override
    public void assignRolesToUser(String auth0UserId, List<String> roles) {

    }

    @Override
    public void deleteUser(String authId) {
        doRequest("/api/v2/users/" + authId, HttpMethod.DELETE, null,null);
    }

    private String checkIfEmailAddressAlreadyExists(String email) {
        ResponseEntity<UserIdResponse[]> response = doRequest("/api/v2/users-by-email?email=" + email + "&fields=user_id", HttpMethod.GET, null, UserIdResponse[].class);
        assert response.getBody() != null;
        List<UserIdResponse> list = Arrays.asList(response.getBody());
        if (list.size() != 0) {
            return list.get(0).getUserId();
        }
        return null;
    }

    private <T> ResponseEntity<T> doRequest(String endpoint, HttpMethod httpMethod, Object body, Class<T> returnValue) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(getToken());
        HttpEntity<?> httpEntity = new HttpEntity<>(body, httpHeaders);
        if (endpoint.startsWith("/")) {
            endpoint = endpoint.substring(1);
        }
        return restTemplate.exchange(authSettings.getIssuer() + endpoint, httpMethod, httpEntity, returnValue);
    }

    private String getToken() {
        if (token == null || new Date().after(token.getExpiresAt())) {
            requestNewToken();
        }
        return this.token.getAccessToken();
    }

    private void requestNewToken() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", authSettings.getClientId());
        body.add("client_secret", authSettings.getClientSecret());
        body.add("audience", authSettings.getIssuer() + "api/v2/");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<GetTokenResponse> response = restTemplate.exchange(authSettings.getIssuer() + "oauth/token", HttpMethod.POST, entity, GetTokenResponse.class);

        GetTokenResponse getTokenResponse = response.getBody();
        assert getTokenResponse != null;
        assert getTokenResponse.getAccessToken() != null;
        assert getTokenResponse.getScope() != null;
        assert getTokenResponse.getTokenType() != null;
        this.token = new Token();
        this.token.setAccessToken(getTokenResponse.getAccessToken());
        this.token.setExpiresIn(getTokenResponse.getExpiresIn());
        if(getTokenResponse.getScope() != null) {
            this.token.setScopes(Arrays.asList(getTokenResponse.getScope().split(" ")));
        }
        this.token.setTokenType(getTokenResponse.getTokenType());
        this.token.setAcquiredAt(new Date());
    }

}
