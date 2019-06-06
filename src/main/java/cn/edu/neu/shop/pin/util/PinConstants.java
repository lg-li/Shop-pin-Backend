package cn.edu.neu.shop.pin.util;

public class PinConstants {

    public static final String WECHAT_APP_ID = "wx2c6c6a9247cd39b0";
    public static final String WECHAT_SECRET_KEY = "94b2114db2aa49376df54fc6e0969962";

    // webSocket相关配置
    // 链接地址
    public static final String WEBSOCKET_PATH_PREFIX = "/ws-push";
    public static final String WEBSOCKET_PATH = "/endpointWisely";
    // 消息代理路径
    public static final String WEBSOCKET_BROADCAST_PATH = "/topic";
    // 前端发送给服务端请求地址
    public static final String FORE_TO_SERVER_PATH = "/welcome";
    // 服务端生产地址,客户端订阅此地址以接收服务端生产的消息
    public static final String PRODUCER_PATH = "/topic/public";
    // 点对点消息推送地址前缀
    public static final String P2P_PUSH_BASE_PATH = "/user";
    // 点对点消息推送地址后缀,最后的地址为/user/用户识别码/msg
    public static final String P2P_PUSH_PATH = "/msg";

    // 状态码
    public class StatusCode {
        public static final int SUCCESS = 200;
        public static final int INVALID_CREDENTIAL = 401;
        public static final int PERMISSION_DENIED = 403;
        public static final int INTERNAL_ERROR = 500;
    }

    public class ResponseMessage {
        public static final String SUCCESS = "请求成功";
        public static final String INVALID_CREDENTIAL = "错误的登录凭据";
        public static final String PERMISSION_DENIED = "权限不足";
        public static final String INTERNAL_ERROR = "服务器错误";
    }

    public class PayType{
        public static final String WEICHAT = "WEICHAT";
        public static final String BALANCE = "BALANCE";
        public static final String BOTH = "WEICHAT BALANCE";
    }

    public class ProductStatus{
        public static final int ALARM = 10;
    }

}
