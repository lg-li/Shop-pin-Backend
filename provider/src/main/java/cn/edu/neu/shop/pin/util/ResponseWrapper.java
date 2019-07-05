package cn.edu.neu.shop.pin.util;

import com.alibaba.fastjson.JSONObject;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ResponseWrapper {

    private static String hostAddress;

    static {
        InetAddress address;
        try {
            address = InetAddress.getLocalHost();
            hostAddress = address.getHostAddress(); //返回IP地址
        } catch (UnknownHostException e) {
            e.printStackTrace();
            hostAddress = "pin";
        }

    }

    public static JSONObject wrap(int code, String message, Object data) {
        JSONObject returnJson = new JSONObject();
        returnJson.put("code", code);
        returnJson.put("message", message);
        returnJson.put("data", data);
        return wrapCurrentServerInfo(returnJson);
    }

    public static JSONObject successJSONWithToken(String token, Object data) {
        JSONObject returnJson = new JSONObject();
        returnJson.put("code", PinConstants.StatusCode.SUCCESS);
        returnJson.put("message", "Token needs update.");
        returnJson.put("data", data);
        returnJson.put("token", token);
        return wrapCurrentServerInfo(returnJson);
    }

    private static JSONObject wrapCurrentServerInfo(JSONObject inputJSON) {
        inputJSON.put("serverHost", hostAddress);
        return inputJSON;
    }
}
