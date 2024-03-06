package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserLogoutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;


/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
@RestController
public class UserController {

  private final UserService userService;

  private final Logger log = LoggerFactory.getLogger(UserController.class);

  UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/users")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<UserGetDTO> getAllUsers() {
    // fetch all users in the internal representation
    List<User> users = userService.getUsers();
    List<UserGetDTO> userGetDTOs = new ArrayList<>();

    // convert each user to the API representation
    for (User user : users) {
      userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
    }
    return userGetDTOs;
  }

  @PostMapping("/users")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {
    // convert API user to internal representation
    User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

    // create user
    User createdUser = userService.createUser(userInput);
    // convert internal representation of user back to API
    return DTOMapper.INSTANCE.convertEntityToUserDTO(createdUser);
  }
  @PutMapping("/users/login")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserGetDTO loginUser(@RequestBody UserPostDTO userPostDTO) {
      // convert API user to internal representation
      User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

      // create user
      User loggedInUser = userService.loginUser(userInput);
      // convert internal representation of user back to API
      log.debug("Login successful");
      return DTOMapper.INSTANCE.convertEntityToUserGetDTO(loggedInUser);


  }
  @PutMapping("/logout")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void logoutUser(@RequestBody UserLogoutDTO userLogoutDTO) {
      // convert API user to internal representation
      User userInput = DTOMapper.INSTANCE.convertUserLogoutDTOtoEntity(userLogoutDTO);
      // create user
      userService.logoutUser(userInput);

      log.debug("Logout Controller successful");
      // convert internal representation of user back to API
      //return DTOMapper.INSTANCE.convertEntityToUserPutDTO(loggedOutUser);
  }
  @GetMapping("/users/{id}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserGetDTO getUser(@PathVariable("id") String id) {
      Long idLong = convertStringToLong(id);
      assert idLong != null;
      User foundUser = userService.getUser(idLong);
      return DTOMapper.INSTANCE.convertEntityToUserGetDTO(foundUser);
  }

  private long  convertStringToLong(String id) {
      Long idLong;

      try {
          idLong = Long.parseLong(id);
      } catch (NumberFormatException e){
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Your search couldn't be found, make sure to use integers and not other elements");
      }
      return idLong;
  }

    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void editUser(@RequestBody UserPutDTO userPutDTO, @PathVariable("id") String id) {
        System.out.println("ID:" + id);
        Long idLong = convertStringToLong(id);
        assert idLong != null;
        System.out.println(userPutDTO);
        User UserInput = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);
        userService.editUser(UserInput, idLong);
        System.out.println("BIRTHDAY:" +userPutDTO.getBirthday());

        //return DTOMapper.INSTANCE.convertEntityToUserPutDTO(foundUser);
    }
}
