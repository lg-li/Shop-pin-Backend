package cn.edu.neu.shop.pin.dto;

import cn.edu.neu.shop.pin.model.PinRole;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class UserDataDTO {

    @ApiModelProperty(position = 2)
    List<PinRole> roles;
    @ApiModelProperty(position = 0)
    private String email;
    @ApiModelProperty(position = 1)
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<PinRole> getRoles() {
        return roles;
    }

    public void setRoles(List<PinRole> roles) {
        this.roles = roles;
    }
}
