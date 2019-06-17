package cn.edu.neu.shop.pin.controller.admin;

import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.service.security.UserService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/manager")
public class InitialController {

    private final UserService userService;

    public InitialController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/user/info")
    public JSONObject defaultLogin(HttpServletRequest req) {
        try {
            PinUser user = userService.whoAmI(req);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }
}
