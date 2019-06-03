package cn.edu.neu.shop.pin.util;

public class PinConstants {

    public static final String WECHAT_APP_ID = "";
    public static final String WECHAT_SECRET_KEY = "";

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

}
