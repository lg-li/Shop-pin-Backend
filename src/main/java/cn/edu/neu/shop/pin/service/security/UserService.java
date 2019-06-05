package cn.edu.neu.shop.pin.service.security;

import cn.edu.neu.shop.pin.service.UserRoleListTransferService;
import cn.edu.neu.shop.pin.exception.CredentialException;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.model.PinWechatUser;
import cn.edu.neu.shop.pin.security.JwtTokenProvider;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

/**
 * @author ydy, llg
 */
@Service
public class UserService extends AbstractService<PinUser> {

    @Autowired
    private UserRoleListTransferService userRoleListTransferService;

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
     * @throws CredentialException 凭据错误异常
     */
    public String signIn(Integer id, String password) throws CredentialException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(id, password));
            return jwtTokenProvider.createToken(id, userRoleListTransferService.findById(id).getRoles());
        } catch (AuthenticationException e) {
            e.printStackTrace();
            throw new CredentialException("Invalid id/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public String signInFromWechatUser(PinWechatUser wechatUser) {
        PinUser pinUser = findById(wechatUser.getUserId());
        if(pinUser == null) {
            return null;
        }
        return jwtTokenProvider.createToken(pinUser.getId(), userRoleListTransferService.findById(wechatUser.getUserId()).getRoles());
    }


    /**
     * 注册并获取新用户token
     * @param phone 手机号
     * @param email 邮箱
     * @param password 密码
     * @param avatarUrl 头像链接
     * @param nickname 昵称
     * @param currentIp 当前IP
     * @param gender 性别
     * @return token
     */
    public String signUpAndGetToken(String phone, String email, String password, String avatarUrl, String nickname, String currentIp, Integer gender) {
        PinUser pinUser = fillInCurrentTimeStampToUser(phone, email, password, avatarUrl, nickname, currentIp, gender);
        return signUp(pinUser);
    }

    /**
     * 注册并获取新用户实体对象
     * @param phone 手机号
     * @param email 邮箱
     * @param password 密码
     * @param avatarUrl 头像链接
     * @param nickname 昵称
     * @param currentIp 当前IP
     * @param gender 性别
     * @return 实体对象
     */
    public PinUser signUpAndGetNewPinUser(String phone, String email, String password, String avatarUrl, String nickname, String currentIp, Integer gender) {
        PinUser pinUser = fillInCurrentTimeStampToUser(phone, email, password, avatarUrl, nickname, currentIp, gender);
        signUp(pinUser);
        return pinUser;
    }

    private PinUser fillInCurrentTimeStampToUser(String phone, String email, String password, String avatarUrl, String nickname, String currentIp, Integer gender) {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        return new PinUser(/*null, phone, email, password, currentTimestamp, currentTimestamp, currentTimestamp, avatarUrl, nickname, new BigDecimal(0), 0, currentIp, currentIp, gender*/);
    }

    /**
     * 注册用户
     *
     * @param user 用户信息（密码传入时保持明文）
     * @return 登录后 Token
     */
    private String signUp(PinUser user) {
        if (!userRoleListTransferService.existsById(user.getId())) {
            user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
            userRoleListTransferService.save(user);
            return jwtTokenProvider.createToken(user.getId(), user.getRoles());
        } else {
            throw new CredentialException("Id is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public void delete(Integer id) {
        userRoleListTransferService.deleteById(id);
    }

    public PinUser search(Integer id) {
        PinUser user = userRoleListTransferService.findById(id);
        if (user == null) {
            throw new CredentialException("The user doesn't exist", HttpStatus.NOT_FOUND);
        }
        return user;
    }

    public PinUser whoAmI(HttpServletRequest req) {
        return userRoleListTransferService.findById(jwtTokenProvider.getId(jwtTokenProvider.resolveToken(req)));
    }

    public String refresh(Integer id) {
        return jwtTokenProvider.createToken(id, userRoleListTransferService.findById(id).getRoles());
    }

    public PinUser findByEmail (String email) {
        return findBy("email", email);
    }

    public PinUser findByPhone (String phone) {
        return findBy("phone", phone);
    }

    public PinUser findByEmailOrPhone (String emailOrPhone) {
        PinUser byEmail = findByEmail(emailOrPhone);
        if(byEmail != null) {
            return byEmail;
        }
        return findByPhone(emailOrPhone);
    }

}
