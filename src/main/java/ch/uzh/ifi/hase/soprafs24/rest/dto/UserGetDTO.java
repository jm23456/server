package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;

import java.time.LocalDate;

public class UserGetDTO {

  private Long id;
  private String name;
  private String username;

  public String password;
  private UserStatus status;

  private LocalDate birthday;

  private LocalDate creationdate;

  private String token;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {this.password = password; }

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }
  public String getToken(){return token;}
  public void setToken(String token) {this.token = token;}

    public LocalDate getBirthday() {return birthday;}

    public void setBirthday(LocalDate birthday) {this.birthday = birthday;}
    public LocalDate getCreationdate() {return creationdate;}

    public void setCreationdate(LocalDate creationdate) {this.creationdate = creationdate;}
}
