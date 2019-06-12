package cn.edu.neu.shop.pin.controller;

import cn.edu.neu.shop.pin.mapper.PinUserMapper;
import cn.edu.neu.shop.pin.mapper.PinUserRoleMapper;
import cn.edu.neu.shop.pin.model.PinRole;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.model.PinUserRole;
import cn.edu.neu.shop.pin.security.JwtTokenProvider;
import cn.edu.neu.shop.pin.service.UserRoleListTransferService;
import cn.edu.neu.shop.pin.service.security.UserService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ydy
 */
@RestController
@RequestMapping(value = "/sign-up")
public class SignUpController {

    private final UserService userService;

    private final PinUserMapper pinUserMapper;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserRoleListTransferService userRoleListTransferService;

    private final PinUserRoleMapper pinUserRoleMapper;

    public SignUpController(UserService userService, PinUserMapper pinUserMapper, JwtTokenProvider jwtTokenProvider, UserRoleListTransferService userRoleListTransferService, PinUserRoleMapper pinUserRoleMapper) {
        this.userService = userService;
        this.pinUserMapper = pinUserMapper;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRoleListTransferService = userRoleListTransferService;
        this.pinUserRoleMapper = pinUserRoleMapper;
    }

    /**
     * 默认注册
     * @param signUpInfo 登录信息JSON
     * @return 响应JSON
     */
    @PostMapping("/default")
    public JSONObject signUpDefault(@RequestBody JSONObject signUpInfo) {
        try {
            //设置角色
            JSONArray roles = signUpInfo.getJSONArray("roles");
            String token = userService.signUpAndGetToken(
                    signUpInfo.getString("phone"),
                    signUpInfo.getString("email"),
                    signUpInfo.getString("password"),
                    signUpInfo.getString("avatarUrl"),
                    signUpInfo.getString("nickname"),
                    signUpInfo.getString("currentIp"),
                    signUpInfo.getInteger("gender"),
                    PinUser.transferRoleIntegerToPinRole(roles.toJavaList(PinRole.class)));
            //如果在这里调findId，没有role，会报空指针
            Integer id = jwtTokenProvider.getId(token);
            PinUser user = pinUserMapper.selectByPrimaryKey(id);
            for (int i = 0; i < roles.size(); i++) {
                PinUserRole role = new PinUserRole(user.getId(), roles.getInteger(i));
                pinUserRoleMapper.insert(role);
            }
            user = userRoleListTransferService.findById(jwtTokenProvider.getId(token));
            //创建JSONObject
            JSONObject data = new JSONObject();
            data.put("user", user);
            data.put("token", token);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }
}
