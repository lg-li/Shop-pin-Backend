package cn.edu.neu.shop.pin.security;

import cn.edu.neu.shop.pin.customer.service.UserRoleListTransferService;
import cn.edu.neu.shop.pin.mapper.PinUserMapper;
import cn.edu.neu.shop.pin.model.PinRole;
import cn.edu.neu.shop.pin.model.PinUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ydy
 */

@Service
public class MyUserDetails implements UserDetailsService {

    @Autowired
    private UserRoleListTransferService userMapper;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        final PinUser pinUser = userMapper.findById(id);

        if (pinUser == null) {
            throw new UsernameNotFoundException("User '" + id + "' not found");
        }

        return org.springframework.security.core.userdetails.User//
                .withUsername(id)//
                .password(pinUser.getPasswordHash())//
                .authorities(pinUser.getRoles())//
                .accountExpired(false)//
                .accountLocked(false)//
                .credentialsExpired(false)//
                .disabled(false)//
                .build();
    }

}
