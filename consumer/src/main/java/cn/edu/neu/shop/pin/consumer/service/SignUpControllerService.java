package cn.edu.neu.shop.pin.consumer.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Service
public interface SignUpControllerService {
    @RequestMapping(value = "/sign-up/default", method = RequestMethod.POST)
    public JSONObject signUpDefault(@RequestBody JSONObject signUpInfo);
}
