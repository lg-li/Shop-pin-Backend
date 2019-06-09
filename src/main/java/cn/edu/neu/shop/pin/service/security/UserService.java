package cn.edu.neu.shop.pin.service.security;

import cn.edu.neu.shop.pin.exception.CredentialException;
import cn.edu.neu.shop.pin.mapper.PinUserRoleMapper;
import cn.edu.neu.shop.pin.model.PinRole;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.model.PinUserRole;
import cn.edu.neu.shop.pin.model.PinWechatUser;
import cn.edu.neu.shop.pin.security.JwtTokenProvider;
import cn.edu.neu.shop.pin.service.UserCreditRecordService;
import cn.edu.neu.shop.pin.service.UserRoleListTransferService;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import com.alibaba.fastjson.JSONObject;
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
import java.util.List;

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

    @Autowired
    private UserCreditRecordService userCreditRecordService;

    @Autowired
    private PinUserRoleMapper pinUserRoleMapper;

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
        if (pinUser == null) {
            return null;
        }
        return jwtTokenProvider.createToken(
                pinUser.getId(),
                userRoleListTransferService
                        .findById(pinUser.getId())
                        .getRoles());
    }


    /**
     * 注册并获取新用户token
     *
     * @param phone     手机号
     * @param email     邮箱
     * @param password  密码
     * @param avatarUrl 头像链接
     * @param nickname  昵称
     * @param currentIp 当前IP
     * @param gender    性别
     * @return token
     */
    public String signUpAndGetToken(String phone, String email, String password, String avatarUrl, String nickname, String currentIp, Integer gender, List<PinRole> list) {
        PinUser pinUser = fillInCurrentTimeStampToUser(phone, email, password, avatarUrl, nickname, currentIp, gender);
        return signUp(pinUser, list);
    }

    /**
     * 注册并获取新用户实体对象
     *
     * @param phone     手机号
     * @param email     邮箱
     * @param password  密码
     * @param avatarUrl 头像链接
     * @param nickname  昵称
     * @param currentIp 当前IP
     * @param gender    性别
     * @return 实体对象
     */
    public PinUser signUpAndGetNewPinUser(String phone, String email, String password, String avatarUrl, String nickname, String currentIp, Integer gender, List<PinRole> roleList) {
        PinUser pinUser = fillInCurrentTimeStampToUser(phone, email, password, avatarUrl, nickname, currentIp, gender);
        signUp(pinUser, roleList);
        return pinUser;
    }

    private PinUser fillInCurrentTimeStampToUser(String phone, String email, String password, String avatarUrl, String nickname, String currentIp, Integer gender) {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        return new PinUser(phone, email, password, currentTimestamp, currentTimestamp, currentTimestamp, avatarUrl, nickname, new BigDecimal(0), 0, currentIp, currentIp, gender);
    }

    /**
     * 注册用户
     *
     * @param user 用户信息（密码传入时保持明文）
     * @return 登录后 Token
     */
    private String signUp(PinUser user, List<PinRole> roleList) {
        if (!userRoleListTransferService.existsById(user.getId())) {
            user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
            userRoleListTransferService.save(user);
            // 插入权限到权限表
            for (PinRole roleToAppend : roleList) {
                PinUserRole role = new PinUserRole(user.getId(), roleToAppend.ordinal());
                pinUserRoleMapper.insert(role);
            }
            return jwtTokenProvider.createToken(user.getId(), roleList);
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
        return whoDoesThisTokenBelongsTo(jwtTokenProvider.resolveToken(req));
    }

    public PinUser whoDoesThisTokenBelongsTo(String token) {
        return userRoleListTransferService.findById(jwtTokenProvider.getId(token));
    }

    public String refresh(Integer id) {
        return jwtTokenProvider.createToken(id, userRoleListTransferService.findById(id).getRoles());
    }

    public JSONObject getUserInfoByUserId(Integer userId) {
        JSONObject data = new JSONObject();
        data.put("user", findById(userId));
        Boolean hasCheckedIn = userCreditRecordService.hasCheckedIn(userId);
        data.put("hasCheckedIn", hasCheckedIn);
        return data;
    }

    public PinUser findByEmail(String email) {
        return findBy("email", email);
    }

    public PinUser findByPhone(String phone) {
        return findBy("phone", phone);
    }

    public PinUser findByEmailOrPhone(String emailOrPhone) {
        if (emailOrPhone == null || emailOrPhone.equals("")) {
            // 非法输入
            return null;
        }
        PinUser byEmail = findByEmail(emailOrPhone);
        if (byEmail != null) {
            return byEmail;
        }
        return findByPhone(emailOrPhone);
    }

}
