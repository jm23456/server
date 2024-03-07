package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class UserService {

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;

  @Autowired
  public UserService(@Qualifier("userRepository") UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<User> getUsers() {
    return this.userRepository.findAll();
  }

  public User createUser(User newUser) {
    newUser.setPassword(newUser.getPassword());
    newUser.setStatus(UserStatus.ONLINE);
    newUser.setToken(UUID.randomUUID().toString());
    newUser.setCreationdate(LocalDate.now());
    checkIfUserExists(newUser);
    // saves the given entity but data is only persisted in the database once
    // flush() is called
    newUser = userRepository.save(newUser);
    userRepository.flush();

    log.debug("Created Information for User: {}", newUser);
    return newUser;
  }

  /**
   * This is a helper method that will check the uniqueness criteria of the
   * username and the name
   * defined in the User entity. The method will do nothing if the input is unique
   * and throw an error otherwise.
   *
   * @param userToBeCreated
   * @throws org.springframework.web.server.ResponseStatusException
   * @see User
   */
  private void checkIfUserExists(User userToBeCreated) {
    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
    User userByName = userRepository.findByName(userToBeCreated.getName());

    String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
    if (userByUsername != null && userByName != null) {
      throw new ResponseStatusException(HttpStatus.CONFLICT,
          String.format(baseErrorMessage, "username and the name", "are"));
    } else if (userByUsername != null) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, "username", "is"));
    } else if (userByName != null) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, "name", "is"));
    }
  }
  public User loginUser(User userToBeChecked) {
      User userByUsername = userRepository.findByUsername(userToBeChecked.getUsername());

      if (userByUsername == null) {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This Username does not exist.");
      } else if (!Objects.equals(userToBeChecked.getPassword(), userByUsername.getPassword())) {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password not correct.");
      }else {
          userByUsername.setStatus(UserStatus.ONLINE);
          userByUsername.setToken(UUID.randomUUID().toString());
          return userByUsername;}
  }

  public void logoutUser(User userToLogout) {
      User userByToken = userRepository.findByToken(userToLogout.getToken());
      System.out.println(userToLogout.getToken());
      if (userByToken == null){
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This User does not exist.");
      } else {
          userByToken.setStatus(UserStatus.OFFLINE);

          log.debug("Logout Service successful");
      }
  }

  public User getUser(Long id) {
      User foundUser = this.userRepository.findById(id).orElse(null);
      if (foundUser == null){
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user you searched for doesn't exist");
      }
      return foundUser;
  }
  public User checkUser(User UserToCheck) {
      if (UserToCheck.getStatus() == UserStatus.ONLINE){
          return UserToCheck;
      }else {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This User is not logged in. You cannot see the User Profile");
      }
  }

  public void editUser(User changedUser, Long id) {
      User foundUser= getUser(id);
      System.out.println("Give me my USERNAME" +changedUser.getUsername());
      User UserByUsername = userRepository.findByUsername(changedUser.getUsername());
      System.out.println("USERNAME" +changedUser.getUsername());
      if (!changedUser.getToken().equals(foundUser.getToken())) {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You are not logged in as the user you want to edit.");
      }
      if (changedUser.getUsername() != null && changedUser.getBirthday() != null) {
          foundUser.setUsername(changedUser.getUsername());
          foundUser.setBirthday(changedUser.getBirthday());
      }
      else if (changedUser.getUsername() == null && changedUser.getBirthday() != null) {
          foundUser.setBirthday(changedUser.getBirthday());
      }
      else {
          foundUser.setUsername(changedUser.getUsername());
      }
      System.out.println("CHANGEDUSER" +foundUser);

  }
}
