package cn.edu.neu.shop.pin.customer.service.security;

import cn.edu.neu.shop.pin.customer.service.UserRoleListTransferService;
import cn.edu.neu.shop.pin.exception.CredentialException;
import cn.edu.neu.shop.pin.model.PinUser;
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
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author ydy
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
    public String signIn(String id, String password) throws CredentialException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(id, password));
            return jwtTokenProvider.createToken(id, userRoleListTransferService.findById(id).getRoles());
        } catch (AuthenticationException e) {
            e.printStackTrace();
            throw new CredentialException("Invalid id/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public String signIn(Integer id, String password) throws CredentialException {
        return signIn(String.valueOf(id), password);
    }

    public String signUp(String phone, String email, String password, String avatarUrl, String nickname, BigDecimal balance, Integer credit, String currentIp, Integer gender) {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        PinUser pinUser = new PinUser(null, phone, email, password, currentTimestamp, currentTimestamp, currentTimestamp, avatarUrl, nickname, new BigDecimal(0), 0, currentIp, currentIp, gender);
        return signUp(pinUser);
    }

    /**
     * 注册用户
     *
     * @param user 用户信息（密码传入时保持明文）
     * @return 登录后 Token
     */
    private String signUp(PinUser user) {
        if (!userRoleListTransferService.existsById(user.getId().toString())) {
            user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
            userRoleListTransferService.save(user);
            return jwtTokenProvider.createToken(user.getId().toString(), user.getRoles());
        } else {
            throw new CredentialException("Id is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public void delete(String id) {
        userRoleListTransferService.deleteById(id);
    }

    public PinUser search(String id) {
        PinUser user = userRoleListTransferService.findById(id);
        if (user == null) {
            throw new CredentialException("The user doesn't exist", HttpStatus.NOT_FOUND);
        }
        return user;
    }

    public PinUser whoAmI(HttpServletRequest req) {
        return userRoleListTransferService.findById(jwtTokenProvider.getId(jwtTokenProvider.resolveToken(req)));
    }

    public String refresh(String id) {
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
