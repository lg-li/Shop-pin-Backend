package cn.edu.neu.shop.pin.util;

/**
 * 微信基础URL链接
 *
 * @author LLG
 */
public class WechatConstants {

    public static final String WECHAT_ACCESS_TOKEN = "pin-wechat-access-token-cache";

    /**
     * 请求URL之获取jsapi_ticket
     */
    public static final String PAGE_URL_SIGN = "jsapi_ticket={0}&noncestr={1}&timestamp={2}&url={3}";

    /**
     * 请求URL之获取access_token
     */
    public static final String BASE_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}";

}
