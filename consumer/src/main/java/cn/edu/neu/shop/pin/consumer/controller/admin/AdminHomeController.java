package cn.edu.neu.shop.pin.consumer.controller.admin;

import cn.edu.neu.shop.pin.consumer.factory.FeignClientFactory;
import cn.edu.neu.shop.pin.consumer.service.admin.AdminCommentControllerService;
import cn.edu.neu.shop.pin.consumer.service.admin.AdminHomeControllerService;
import com.alibaba.fastjson.JSONObject;
import feign.Client;
import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Import(FeignClientsConfiguration.class)
@RestController
public class AdminHomeController {
    private AdminHomeControllerService adminHomeControllerService;

    @Autowired
    public AdminHomeController(
            Decoder decoder, Encoder encoder, Client client) {
        this.adminHomeControllerService = FeignClientFactory.getFeignClient(
                decoder, encoder, client,
                AdminHomeControllerService.class);
    }

    @GetMapping("/home")
    public JSONObject getCommentSevenDays() {
        return adminHomeControllerService.getCommentSevenDays();
    }
}
