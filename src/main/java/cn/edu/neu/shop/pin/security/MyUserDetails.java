package cn.edu.neu.shop.pin.security;

import cn.edu.neu.shop.pin.mapper.PinUserMapper;
import cn.edu.neu.shop.pin.model.PinUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetails implements UserDetailsService {

  @Autowired
  private PinUserMapper userMapper;

  @Override
  public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
    final PinUser user = userMapper.findById(id);

    if (user == null) {
      throw new UsernameNotFoundException("User '" + id + "' not found");
    }

    return org.springframework.security.core.userdetails.User//
        .withUsername(id)//
        .password(user.getPasswordHash())//
        .authorities(user.getRoles())//
        .accountExpired(false)//
        .accountLocked(false)//
        .credentialsExpired(false)//
        .disabled(false)//
        .build();
  }

}
