package cn.edu.neu.shop.pin.customer.service.security;

import cn.edu.neu.shop.pin.customer.service.UserRoleListTransferService;
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
    private UserRoleListTransferService userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    //登陆
    public String signin(String id, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(id, password));
            return jwtTokenProvider.createToken(id, userMapper.findById(id).getRoles());
        } catch (AuthenticationException e) {
            e.printStackTrace();
            throw new CustomException("Invalid id/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    //注册
    public String signup(PinUser user) {
        if (!userMapper.existsById(user.getId().toString())) {
            user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
            userMapper.save(user);
            return jwtTokenProvider.createToken(user.getId().toString(), user.getRoles());
        } else {
            throw new CustomException("Id is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public void delete(String id) {
        userMapper.deleteById(id);
    }

    public PinUser search(String id) {
        PinUser user = userMapper.findById(id);
        if (user == null) {
            throw new CustomException("The user doesn't exist", HttpStatus.NOT_FOUND);
        }
        return user;
    }

    public PinUser whoami(HttpServletRequest req) {
        return userMapper.findById(jwtTokenProvider.getId(jwtTokenProvider.resolveToken(req)));
    }

    public String refresh(String id) {
        return jwtTokenProvider.createToken(id, userMapper.findById(id).getRoles());
    }

}
