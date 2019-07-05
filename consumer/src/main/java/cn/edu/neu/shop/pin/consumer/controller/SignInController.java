package cn.edu.neu.shop.pin.consumer.controller;

import cn.edu.neu.shop.pin.consumer.factory.FeignClientFactory;
import cn.edu.neu.shop.pin.consumer.service.SignInControllerService;
import cn.edu.neu.shop.pin.consumer.service.commons.UserCollectionControllerService;
import cn.edu.neu.shop.pin.consumer.service.security.UserControllerService;
import com.alibaba.fastjson.JSONObject;
import feign.Client;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Import(FeignClientsConfiguration.class)
@RestController
@RequestMapping(value = "/sign-in")
public class SignInController {
    private SignInControllerService signInControllerService;

    @Autowired
    public SignInController(
            Decoder decoder, Encoder encoder, Client client) {
        this.signInControllerService = FeignClientFactory.getFeignClient(
                decoder, encoder, client,
                SignInControllerService.class);
    }

    @PostMapping(value = "/default")
    public JSONObject defaultLogin(@RequestBody JSONObject loginJSON) {
        return signInControllerService.defaultLogin(loginJSON);
    }

    @PostMapping(value = "/wechat-mini-program")
    public JSONObject wechatMiniProgramLogin(@RequestBody JSONObject loginJSON) {
        return signInControllerService.wechatMiniProgramLogin(loginJSON);
    }
}
