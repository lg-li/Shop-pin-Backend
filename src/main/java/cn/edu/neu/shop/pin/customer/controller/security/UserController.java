package cn.edu.neu.shop.pin.customer.controller.security;

import cn.edu.neu.shop.pin.customer.service.security.UserService;
import cn.edu.neu.shop.pin.dto.UserDataDTO;
import cn.edu.neu.shop.pin.dto.UserResponseDTO;
import cn.edu.neu.shop.pin.model.PinUser;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.mappers.ModelMapper;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/users")
@Api(tags = "users")
public class UserController {

  @Autowired
  private UserService userService;

  /*@Autowired
  private ModelMapper modelMapper;*/
  //这里在登陆，得到token
  @PostMapping("/signin")
  @ApiOperation(value = "${UserController.signin}")
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 422, message = "Invalid email/password supplied")})
  public String login(//
                      @ApiParam("Email") @RequestParam String email, //
                      @ApiParam("Password") @RequestParam String password) {
    return userService.signin(email, password);
  }

  //这里在注册，保存信息，并且得到token
  @PostMapping("/signup")
  @ApiOperation(value = "${UserController.signup}")
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 403, message = "Access denied"), //
      @ApiResponse(code = 422, message = "Email is already in use"), //
      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public String signup(@ApiParam("Signup User") @RequestBody UserDataDTO user) {
//    PinUser pinUser = new PinUser(user.getEmail(),user.getPassword(),user.getRoles());
//    return userService.signup(pinUser);
      return null;
  }

  //
  @DeleteMapping(value = "/{email}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation(value = "${UserController.delete}")
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 403, message = "Access denied"), //
      @ApiResponse(code = 404, message = "The user doesn't exist"), //
      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public String delete(@ApiParam("Email") @PathVariable String email) {
    userService.delete(email);
    return email;
  }

  @GetMapping(value = "/{email}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation(value = "${UserController.search}", response = /*UserResponseDTO*/PinUser.class)
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 403, message = "Access denied"), //
      @ApiResponse(code = 404, message = "The user doesn't exist"), //
      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public /*UserResponseDTO*/PinUser search(@ApiParam("Email") @PathVariable String email) {
//    return modelMapper.map(userService.search(username), UserResponseDTO.class);
    return userService.search(email);
  }

  @GetMapping(value = "/me")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
  @ApiOperation(value = "${UserController.me}", response = /*UserResponseDTO*/PinUser.class)
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 403, message = "Access denied"), //
      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public /*UserResponseDTO*/PinUser whoami(HttpServletRequest req) {
//    return modelMapper.map(userService.whoami(req), UserResponseDTO.class);
    return userService.whoami(req);
  }

  @GetMapping("/refresh")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
  public String refresh(HttpServletRequest req) {
    return userService.refresh(req.getRemoteUser());
  }

}
