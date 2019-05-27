package cn.edu.neu.shop.pin.util.wechat;

public class WeChatCredentialExchangeException extends Exception{
    public WeChatCredentialExchangeException(int errCode, String errMsg) {
        super("[WeChat] [微信登录交换凭据异常]: 异常码 ["+errCode + "] / 信息:" + errMsg);
    }
}
