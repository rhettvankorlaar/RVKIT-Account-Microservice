package nl.rvkit.accountmicroservice.user;

import io.swagger.annotations.Api;
import nl.rvkit.accountmicroservice.user.dto.RegistrationDTO;
import nl.rvkit.accountmicroservice.user.dto.UserDTO;
import nl.rvkit.accountmicroservice.util.Response;
import nl.rvkit.accountmicroservice.util.ResponseBuilder;
import nl.rvkit.accountmicroservice.util.Routes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Routes.User.USERS)
@Api(tags = "User Endpoint", value = "Users")
public class UserController {
    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Response<UserDTO> createUser(@RequestBody RegistrationDTO registrationDTO){
        //Add user to database and Auth0
        var userDTO = userService.createUser(registrationDTO);

        return ResponseBuilder.created().data(userDTO).build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Response<List<UserDTO>> getUsers(){
        return ResponseBuilder.ok().data(userService.getAllUsers()).build();
    }

    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Response<UserDTO> getUser(@PathVariable int userId){
        return ResponseBuilder.ok().data(userService.getUserById((long) userId)).build();
    }

    @GetMapping(value = "/userinfo", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Response<UserDTO> getUserInfo(Authentication authentication) {
        String authId = authentication.getName();
        return ResponseBuilder.ok()
                .data(userService.getUserByAuthId(authId))
                .build();
    }

    @DeleteMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Response<Boolean> deleteUser(@PathVariable int userId){
        userService.deleteUser((long) userId);
        return ResponseBuilder.accepted().data(true).build();
    }
}
