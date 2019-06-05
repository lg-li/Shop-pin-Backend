package cn.edu.neu.shop.pin.security;

import cn.edu.neu.shop.pin.service.UserRoleListTransferService;
import cn.edu.neu.shop.pin.model.PinUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author ydy
 */

@Service
public class MyUserDetails implements UserDetailsService {

    @Autowired
    private UserRoleListTransferService userMapper;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        final PinUser pinUser = userMapper.findById((Integer)Integer.parseInt(id));

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
