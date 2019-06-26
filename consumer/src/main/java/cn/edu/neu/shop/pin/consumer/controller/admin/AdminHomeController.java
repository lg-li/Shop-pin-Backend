package cn.edu.neu.shop.pin.consumer.controller.admin;

import cn.edu.neu.shop.pin.consumer.service.admin.AdminCommentControllerService;
import cn.edu.neu.shop.pin.consumer.service.admin.AdminHomeControllerService;
import com.alibaba.fastjson.JSONObject;
import feign.Client;
import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminHomeController {
    @Autowired
    AdminHomeControllerService adminHomeControllerService;

    @Autowired
    public AdminHomeController(
            Decoder decoder, Encoder encoder, Client client) {
        this.adminHomeControllerService = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .target(AdminHomeControllerService.class, "http://Pin-Provider");
    }

    @GetMapping("/home")
    public JSONObject getCommentSevenDays() {
        return adminHomeControllerService.getCommentSevenDays();
    }
}
