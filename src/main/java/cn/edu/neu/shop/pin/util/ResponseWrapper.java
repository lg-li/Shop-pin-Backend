package cn.edu.neu.shop.pin.util;

import com.alibaba.fastjson.JSONObject;

public class ResponseWrapper {
    public static JSONObject wrap(int code, String message, Object data){
        JSONObject returnJson = new JSONObject();
        returnJson.put("code", code);
        returnJson.put("message", message);
        returnJson.put("data", data);
        return returnJson;
    }

    public static JSONObject successJSONWithToken (String token, Object data) {
        JSONObject returnJson = new JSONObject();
        returnJson.put("code", PinConstants.StatusCode.SUCCESS);
        returnJson.put("message", "Token needs update.");
        returnJson.put("data", data);
        returnJson.put("token", token);
        return returnJson;
    }
}
