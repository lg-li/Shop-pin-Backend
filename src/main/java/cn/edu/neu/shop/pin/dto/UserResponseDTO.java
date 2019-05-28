package cn.edu.neu.shop.pin.dto;

import cn.edu.neu.shop.pin.model.PinRole;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class UserResponseDTO {

  @ApiModelProperty(position = 0)
  private Integer id;
  @ApiModelProperty(position = 1)
  private String username;
  @ApiModelProperty(position = 2)
  private String email;
  @ApiModelProperty(position = 3)
  List<PinRole> roles;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public List<PinRole> getRoles() {
    return roles;
  }

  public void setRoles(List<PinRole> roles) {
    this.roles = roles;
  }

}
