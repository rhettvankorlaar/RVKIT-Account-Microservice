package nl.rvkit.accountmicroservice.user;

import nl.rvkit.accountmicroservice.user.dto.RegistrationDTO;
import nl.rvkit.accountmicroservice.user.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IUserService {

    UserDTO updateUser(UserDTO userDTO);

    void deleteUser(Long userId);

    UserDTO createUser(RegistrationDTO registrationDTO);

    UserDTO getUserById(Long userId);

    UserDTO getUserByAuthId(String authId);

    List<UserDTO> getAllUsers();

    UserDTO createAuthAccountForUser(Long userId);
}
