package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@SpringBootTest
public class UserServiceIntegrationTest {

  @Qualifier("userRepository")
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  @BeforeEach
  public void setup() {
    userRepository.deleteAll();
  }

  @Test
  public void createUser_validInputs_success() {
    // given
    assertNull(userRepository.findByUsername("testUsername"));

    User testUser = new User();
    testUser.setName("testName");
    testUser.setUsername("testUsername");

    // when
    User createdUser = userService.createUser(testUser);

    // then
    assertEquals(testUser.getId(), createdUser.getId());
    assertEquals(testUser.getName(), createdUser.getName());
    assertEquals(testUser.getUsername(), createdUser.getUsername());
    assertNotNull(createdUser.getToken());
    assertEquals(UserStatus.ONLINE, createdUser.getStatus());
  }

  @Test
  public void createUser_duplicateUsername_throwsException() {
    assertNull(userRepository.findByUsername("testUsername"));

    User testUser = new User();
    testUser.setName("testName");
    testUser.setUsername("testUsername");
    User createdUser = userService.createUser(testUser);

    // attempt to create second user with same username
    User testUser2 = new User();

    // change the name but forget about the username
    testUser2.setName("testName2");
    testUser2.setUsername("testUsername");

    // check that an error is thrown
    assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser2));
  }

  //test 3
  @Test
  public void getUser_validInputs_success() {
      // given
      assertNull(userRepository.findByUsername("testUsername"));

      User testUser = new User();
      testUser.setName("testName");
      testUser.setUsername("testUsername");
      testUser.setPassword("password");
      User createdUser = userService.createUser(testUser);
      //get id from createdUser (testUser)
      Long id = createdUser.getId();

      // get User from Id
      User user2 = userService.getUser(id);

      // then
      assertEquals(UserStatus.ONLINE, user2.getStatus());
      assertEquals(user2.getId(), createdUser.getId());
      assertEquals(user2.getUsername(), createdUser.getUsername());
  }

  //test 4
  @Test
  public void getUser_wrongId_throwsException() {
      assertNull(userRepository.findByUsername("testUsername"));

      //given (bizeli unnötig da)
      User testUser = new User();
      testUser.setName("testName");
      testUser.setUsername("testUsername");
      User createdUser = userService.createUser(testUser);

      // create ID
      Long testUserID = 1L;

      // check that an error is thrown because ID doesn't exist.
      assertThrows(ResponseStatusException.class, () -> userService.getUser(testUserID));
  }

  //Test 5
  @Test
  public void editUser_validInputs_success() {
      // given
      assertNull(userRepository.findByUsername("newUsername"));

      User testUser = new User();
      testUser.setName("newName");
      testUser.setUsername("newUsername");
      testUser.setPassword("newPassword");
      testUser.setToken("validToken");
      User changedUser = userService.createUser(testUser);

      //get id from changedUser
      Long id = changedUser.getId();
      //save birthday you want to add to user
      LocalDate birthday = LocalDate.of(2022,10,8);
      // change birthday
      userService.editUser(changedUser, id);

      //get updated user by id
      User updatedUser = userService.getUser(id);

      // then
      assertEquals(updatedUser.getBirthday(), changedUser.getBirthday());
      assertEquals(updatedUser.getUsername(), changedUser.getUsername());
  }

  //test 6
  @Test
  public void editUser_wrongToken_throwsException() {
      assertNull(userRepository.findByUsername("testUsername"));

      //given
      User testUser = new User();
      testUser.setName("testName");
      testUser.setUsername("testUsername");
      testUser.setPassword("testPassword");
      testUser.setToken("1");
      User changedUser = userService.createUser(testUser);

      // get ID for changedUser
      Long id = changedUser.getId();

      //create new foundUser with different token
      User foundUser = new User();
      foundUser.setToken("0");

      // check that an error is thrown because Tokens don't match
      assertThrows(ResponseStatusException.class, () -> userService.editUser(foundUser, id));
  }
}
