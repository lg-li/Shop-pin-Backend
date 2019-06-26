package cn.edu.neu.shop.pin.consumer.controller;

import cn.edu.neu.shop.pin.consumer.service.SignUpControllerService;
import cn.edu.neu.shop.pin.consumer.service.commons.UserBasicInfoControllerService;
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

@Import(FeignClientsConfiguration.class)
@RestController
@RequestMapping(value = "/sign-up")
public class SignUpController {
    private SignUpControllerService signUpControllerService;

    @Autowired
    public SignUpController(
            Decoder decoder, Encoder encoder, Client client) {
        this.signUpControllerService = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .target(SignUpControllerService.class, "http://Pin-Provider");
    }

    @PostMapping("/default")
    public JSONObject signUpDefault(@RequestBody JSONObject signUpInfo) {
        return signUpControllerService.signUpDefault(signUpInfo);
    }
}
