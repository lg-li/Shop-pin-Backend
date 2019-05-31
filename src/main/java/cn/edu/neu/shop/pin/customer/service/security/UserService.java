package cn.edu.neu.shop.pin.customer.service.security;

import cn.edu.neu.shop.pin.customer.service.UserRoleListTransferService;
import cn.edu.neu.shop.pin.exception.CustomException;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ydy
 */
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

    /**
     * 登录接口
     *
     * @param id       用户 ID
     * @param password 密码明文
     * @return 生成的token
     * @throws CustomException 凭据错误异常
     */
    public String signIn(String id, String password) throws CustomException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(id, password));
            return jwtTokenProvider.createToken(id, userMapper.findById(id).getRoles());
        } catch (AuthenticationException e) {
            e.printStackTrace();
            throw new CustomException("Invalid id/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public String signIn(Integer id, String password) throws CustomException {
        return signIn(String.valueOf(id), password);
    }

    /**
     * 注册用户
     *
     * @param user 用户信息
     * @return 登录后 Token
     */
    public String signUp(PinUser user) {
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

    public PinUser whoAmI(HttpServletRequest req) {
        return userMapper.findById(jwtTokenProvider.getId(jwtTokenProvider.resolveToken(req)));
    }

    public String refresh(String id) {
        return jwtTokenProvider.createToken(id, userMapper.findById(id).getRoles());
    }

}
