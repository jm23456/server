package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;

import java.time.LocalDate;


public class UserPutDTO {

  private String username;

  private String token;

  private LocalDate birthday;

  public LocalDate getBirthday() {return birthday;}
  public void setBirthday(LocalDate birthday) {this.birthday = birthday;}

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getToken() {return token; }

  public void setToken(String token) {this.token = token;}
}
