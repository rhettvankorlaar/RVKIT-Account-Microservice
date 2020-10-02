package nl.rvkit.accountmicroservice.auth;

import nl.rvkit.accountmicroservice.auth.dto.AuthUserDTO;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface IAuthService {
    AuthUserDTO getAuthUser();

    String createAuthUser(AuthUserDTO authUser);

    String requestPasswordResetLink(String auth0UserId);

    void assignRolesToUser(String auth0UserId, List<String> roles);

    void deleteUser(String authId);
}
