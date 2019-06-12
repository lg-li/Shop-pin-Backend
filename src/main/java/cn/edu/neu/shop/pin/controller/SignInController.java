package cn.edu.neu.shop.pin.controller;

import cn.edu.neu.shop.pin.exception.CredentialException;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.service.WechatUserService;
import cn.edu.neu.shop.pin.service.security.UserService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import cn.edu.neu.shop.pin.util.wechat.WeChatCredentialExchangeException;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author llg
 */
@RestController
@RequestMapping(value = "/sign-in")
@CrossOrigin
public class SignInController {

    private final UserService userService;

    private final WechatUserService wechatUserService;

    public SignInController(UserService userService, WechatUserService wechatUserService) {
        this.userService = userService;
        this.wechatUserService = wechatUserService;
    }

    /**
     * 默认登录
     * @param loginJSON 登录JSON
     * @return 响应JSON
     */
    @PostMapping(value = "/default")
    public JSONObject defaultLogin(@RequestBody JSONObject loginJSON) {
        String emailOrPhone = loginJSON.getString("user");
        String password = loginJSON.getString("password");
        PinUser userFound = userService.findByEmailOrPhone(emailOrPhone);
        if (userFound == null) {
            // 非法的用户名/邮箱/手机号
            return ResponseWrapper.wrap(
                    PinConstants.StatusCode.INVALID_CREDENTIAL,
                    PinConstants.ResponseMessage.INVALID_CREDENTIAL,
                    null);
        }
        // System.out.println("Login...");
        String token;
        try {
            token = userService.signIn(userFound.getId(), password);
        } catch (CredentialException ex) {
            // 非法的密码
            return ResponseWrapper.wrap(
                    PinConstants.StatusCode.INVALID_CREDENTIAL,
                    PinConstants.ResponseMessage.INVALID_CREDENTIAL,
                    null);
        }
        return ResponseWrapper.wrap(
                PinConstants.StatusCode.SUCCESS,
                PinConstants.ResponseMessage.SUCCESS,
                token);
    }

    /**
     * 微信小程序登录
     * @param httpServletRequest HttpServlet请求体
     * @param loginJSON 登录JSON
     * @return 响应JSON
     */
    @PostMapping(value = "/wechat-mini-program")
    public JSONObject wechatMiniProgramLogin(HttpServletRequest httpServletRequest, @RequestBody JSONObject loginJSON) {
        String code = loginJSON.getString("code");
        String name = loginJSON.getString("nickName");
        Integer gender = loginJSON.getInteger("gender");
        String avatarUrl = loginJSON.getString("avatarUrl");
        String country = loginJSON.getString("country");
        String province = loginJSON.getString("province");
        String city = loginJSON.getString("city");
        String language = loginJSON.getString("language");
        try {
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS,
                    wechatUserService.signInFormWechatMiniProgram(code, name, gender, avatarUrl, country,
                            province, city, language, httpServletRequest.getRemoteAddr()));
        } catch (WeChatCredentialExchangeException e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INVALID_CREDENTIAL, PinConstants.ResponseMessage.INVALID_CREDENTIAL, null);
        }
    }
}
