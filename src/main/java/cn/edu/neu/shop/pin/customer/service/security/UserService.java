package cn.edu.neu.shop.pin.customer.service.security;

import cn.edu.neu.shop.pin.exception.CustomException;
import cn.edu.neu.shop.pin.mapper.PinUserMapper;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.security.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserService {

  @Autowired
  private PinUserMapper userMapper;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  private AuthenticationManager authenticationManager;



  public String signin(String email, String password) {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
      return jwtTokenProvider.createToken(email, userMapper.findByEmail(email).getRoles());
    } catch (AuthenticationException e) {
      throw new CustomException("Invalid email/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  public String signup(PinUser user) {
    if (!userMapper.existsByEmail(user.getEmail())) {
      user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
      userMapper.save(user);
      return jwtTokenProvider.createToken(user.getEmail(), user.getRoles());
    } else {
      throw new CustomException("Email is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  public void delete(String email) {
    userMapper.deleteByEmail(email);
  }

  public PinUser search(String email) {
    PinUser user = userMapper.findByEmail(email);
    if (user == null) {
      throw new CustomException("The user doesn't exist", HttpStatus.NOT_FOUND);
    }
    return user;
  }

  public PinUser whoami(HttpServletRequest req) {
    return userMapper.findByEmail(jwtTokenProvider.getEmail(jwtTokenProvider.resolveToken(req)));
  }

  public String refresh(String email) {
    return jwtTokenProvider.createToken(email, userMapper.findByEmail(email).getRoles());
  }

}
