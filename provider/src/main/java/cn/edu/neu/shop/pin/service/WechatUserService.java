package cn.edu.neu.shop.pin.service;

import cn.edu.neu.shop.pin.mapper.PinWechatUserMapper;
import cn.edu.neu.shop.pin.model.PinRole;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.model.PinWechatUser;
import cn.edu.neu.shop.pin.service.security.UserService;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import cn.edu.neu.shop.pin.util.wechat.WeChatCredential;
import cn.edu.neu.shop.pin.util.wechat.WeChatCredentialExchangeException;
import cn.edu.neu.shop.pin.util.wechat.WeChatCredentialExchanger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author llg
 */
@Service
public class WechatUserService extends AbstractService<PinWechatUser> {

    private final UserService userService;

    private final PinWechatUserMapper pinWechatUserMapper;

    public WechatUserService(UserService userService, PinWechatUserMapper pinWechatUserMapper) {
        this.userService = userService;
        this.pinWechatUserMapper = pinWechatUserMapper;
    }

    public PinWechatUser findWechatUserByUserId (Integer userId) {
        PinWechatUser example = new PinWechatUser();
        example.setUserId(userId);
        return mapper.selectOne(example);
    }

    /**
     * 使用微信小程序登录
     */
    public String signInFormWechatMiniProgram(String code, String name, int gender, String avatarUrl, String country, String province, String city, String language, String currentIp) throws WeChatCredentialExchangeException {
        final WeChatCredential wechatCredential = WeChatCredentialExchanger.fromMiniProgramCode(code);
        PinWechatUser wechatUser = new PinWechatUser();
        // 根据 OPEN ID 查找客户
        wechatUser.setOpenId(wechatCredential.getOpenId());
        wechatUser = pinWechatUserMapper.selectOne(wechatUser);
        if (wechatUser != null) {
            // 已存在此微信用户
            assignPropertyToWechatUserAndSave(name, gender, avatarUrl, country, province, city, language, wechatUser);
            // 更新头像等信息
            update(wechatUser);

        } else {
            // 微信用户首次登录
            List<PinRole> roleList = new ArrayList<>();
            roleList.add(PinRole.ROLE_USER); // 默认权限
            PinUser newPinUser = userService.signUpAndGetNewPinUser(null, null, "default", avatarUrl, name, currentIp, gender, roleList);
            wechatUser = new PinWechatUser(); // 重新初始化
            wechatUser.setOpenId(wechatCredential.getOpenId());
            // 绑定微信用户与实体用户
            wechatUser.setUserId(newPinUser.getId());
            assignPropertyToWechatUserAndSave(name, gender, avatarUrl, country, province, city, language, wechatUser);
            // 创建基本信息并保存
            save(wechatUser);
        }
        return userService.signInFromWechatUser(wechatUser);
    }

    private void assignPropertyToWechatUserAndSave(String name, int gender, String avatarUrl, String country, String province, String city, String language, PinWechatUser wechatUserToAssign) {
        wechatUserToAssign.setNickname(name);
        wechatUserToAssign.setAvatarUrl(avatarUrl);
        wechatUserToAssign.setGender(gender);
        wechatUserToAssign.setCountry(country);
        wechatUserToAssign.setProvince(province);
        wechatUserToAssign.setCity(city);
        wechatUserToAssign.setLanguage(language);
    }
}
