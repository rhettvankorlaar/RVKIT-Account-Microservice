package nl.rvkit.accountmicroservice.user;

import nl.rvkit.accountmicroservice.auth.IAuthService;
import nl.rvkit.accountmicroservice.auth.dto.AuthUserDTO;
import nl.rvkit.accountmicroservice.user.dto.RegistrationDTO;
import nl.rvkit.accountmicroservice.user.dto.UserDTO;
import nl.rvkit.accountmicroservice.util.Mapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
@Service
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final IAuthService authService;

    public UserService(UserRepository userRepository, IAuthService authService) {
        this.userRepository = userRepository;
        this.authService = authService;
    }

    @Override
    @PreAuthorize("hasPermission('update:user')")
    public UserDTO updateUser(UserDTO userDTO) {
        var user = Mapper.map(userDTO, User.class);
        user = userRepository.save(user);
        return Mapper.map(user, UserDTO.class);
    }

    @Override
    @PreAuthorize("hasPermission('delete:user')")
    public void deleteUser(Long userId) {
        var deleteUser = findUserById(userId);
        userRepository.delete(deleteUser);
        authService.deleteUser(deleteUser.getAuth0UserId());
    }

    @Override
    public UserDTO createUser(RegistrationDTO registrationDTO) {

        if(userRepository.existsByEmail(registrationDTO.getEmail())){
            //Already a user with that email
            throw new IllegalArgumentException("Email already in use!");
        }
        var user = Mapper.map(registrationDTO, User.class);
        user = saveUser(user);
        this.createAuthAccountForUser(user.getId());
        return Mapper.map(user, UserDTO.class);
    }

    @Override
    @PreAuthorize("hasPermission('read:user')")
    public UserDTO getUserById(Long userId) {
        return Mapper.map(findUserById(userId), UserDTO.class);
    }

    @Override
    @PreAuthorize("(hasPermission('read:user') && authentication.name = #authId)")
    public UserDTO getUserByAuthId(String authId) {
        return Mapper.map(findUserByAuthId(authId), UserDTO.class);
    }

    @Override
    @PreAuthorize("hasPermission('read:users')")
    public List<UserDTO> getAllUsers() {
        var allUsers = userRepository.findAll();
        return Mapper.mapAll(allUsers, UserDTO.class);
    }

    @Override
    public UserDTO createAuthAccountForUser(Long userId) {
        User user = findUserById(userId);
        if (user.getAuth0UserId() != null) {
            throw new IllegalStateException("Account already created for user!");
        }
        AuthUserDTO authUserDTO = new AuthUserDTO();

        authUserDTO.setGivenName(user.getFirstName());
        authUserDTO.setFamilyName(user.getFullLastName());
        authUserDTO.setEmail(user.getEmail());
        String authUserId = authService.createAuthUser(authUserDTO);
        user.setAuth0UserId(authUserId);

        this.saveUser(user);
        return Mapper.map(user, UserDTO.class);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User " + userId + " not found!"));
    }

    private User findUserByAuthId(String authId) {
        return userRepository.findByAuth0UserId(authId)
                .orElseThrow(() -> new EntityNotFoundException("User with authId: " + authId + " not found!"));
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }


}
