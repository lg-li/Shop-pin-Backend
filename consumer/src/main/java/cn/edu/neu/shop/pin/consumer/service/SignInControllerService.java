package cn.edu.neu.shop.pin.consumer.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Service
public interface SignInControllerService {
    @RequestMapping(value = "/sign-in/default", method = RequestMethod.POST)
    public JSONObject defaultLogin(@RequestBody JSONObject loginJSON);

    @RequestMapping(value = "/sign-in/wechat-mini-program", method = RequestMethod.POST)
    public JSONObject wechatMiniProgramLogin(@RequestBody JSONObject loginJSON);
}
