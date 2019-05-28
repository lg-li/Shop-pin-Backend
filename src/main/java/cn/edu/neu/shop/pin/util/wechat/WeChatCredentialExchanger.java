package cn.edu.neu.shop.pin.util.wechat;

import cn.edu.neu.shop.pin.util.PinConstants;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class WeChatCredentialExchanger {

    /**
     * (阻塞式)通过code交换微信身份数据
     *
     * @param code 小程序返回的code
     * @return 已交换的凭据数据
     */
    public static WeChatCredential fromMiniProgramCode(String code) throws WeChatCredentialExchangeException {
        WeChatCredential sessionData = new WeChatCredential();
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> vars = new HashMap<String, String>();
        vars.put("appId", PinConstants.WECHAT_APP_ID);
        vars.put("secretKey", PinConstants.WECHAT_SECRET_KEY);
        vars.put("code", code);
        String s = restTemplate.getForObject("https://api.weixin.qq.com/sns/jscode2session?appid={appId}&secret={secretKey}&js_code={code}&grant_type=authorization_code", String.class, vars);
        JSONObject result = JSONObject.parseObject(s);
        System.out.println(result);
        String openId = result.getString("openid");
        if ((!result.containsKey("errcode")) && openId != null) {
            // 成功
            sessionData.setOpenId(openId);
            sessionData.setUnionId(result.getString("unionid"));
            sessionData.setSessionKey(result.getString("session_key"));
            return sessionData;
        } else {
            throw new WeChatCredentialExchangeException(result.getInteger("errcode"), result.getString("errmsg"));
        }
    }

}
