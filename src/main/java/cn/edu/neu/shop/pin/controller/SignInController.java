package cn.edu.neu.shop.pin.controller;

import cn.edu.neu.shop.pin.service.WechatUserService;
import cn.edu.neu.shop.pin.service.security.UserService;
import cn.edu.neu.shop.pin.exception.CredentialException;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import cn.edu.neu.shop.pin.util.wechat.WeChatCredentialExchangeException;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author llg
 */
@RestController
@RequestMapping(value = "/sign-in")
public class SignInController {

    @Autowired
    UserService userService;

    @Autowired
    WechatUserService wechatUserService;

    @PostMapping(value = "/default")
    public JSONObject defaultLogin(@RequestBody JSONObject loginJSON) {
        String emailOrPhone = loginJSON.getString("user");
        String password = loginJSON.getString("password");
        PinUser userFound = userService.findByEmailOrPhone(emailOrPhone);
        if(userFound == null) {
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

    @RequestMapping(value = "/wechat-mini-program")
    public JSONObject wechatMiniProgramLogin(HttpServletRequest request, JSONObject loginJSON) {
        String code = loginJSON.getString("code");
        String name = loginJSON.getString("nickname");
        Integer gender = loginJSON.getInteger("gender");
        String avatarUrl = loginJSON.getString("avatarUrl");
        String country = loginJSON.getString("country");
        String province = loginJSON.getString("province");
        String city = loginJSON.getString("city");
        String language = loginJSON.getString("language");
        try {
            return ResponseWrapper.wrap(
                    PinConstants.StatusCode.SUCCESS,
                    PinConstants.ResponseMessage.SUCCESS,
                    wechatUserService.signInFormWechatMiniProgram(
                            code,
                            name,
                            gender,
                            avatarUrl,
                            country,
                            province,
                            city,
                            language,
                            request.getRemoteAddr()
                    )
            );

        } catch (WeChatCredentialExchangeException e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(
                    PinConstants.StatusCode.INVALID_CREDENTIAL,
                    PinConstants.ResponseMessage.INVALID_CREDENTIAL,
                    null);
        }
    }

}
