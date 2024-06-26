package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.time.LocalDate;

public class UserPostDTO {

  private String name;

  private String username;

  private String password;

  private String token;

  private LocalDate creationdate;

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

  public String getPassword() {return password; }

  public void setPassword(String password) {this.password = password;}

    public String getToken() {return token; }

    public void setToken(String token) {this.token = token;}

    public LocalDate getCreationdate() {return creationdate; }
    public void setCreationdate(LocalDate creationdate) {this.creationdate = creationdate;}
}
