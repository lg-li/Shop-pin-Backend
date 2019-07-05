package cn.edu.neu.shop.pin.consumer.controller.admin;

import cn.edu.neu.shop.pin.consumer.factory.FeignClientFactory;
import cn.edu.neu.shop.pin.consumer.service.admin.AdminStoreControllerService;
import cn.edu.neu.shop.pin.consumer.service.admin.InitialControllerService;
import com.alibaba.fastjson.JSONObject;
import feign.Client;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Import(FeignClientsConfiguration.class)
@RestController
@RequestMapping(value = "/manager")
public class InitialController {
    private InitialControllerService initialControllerService;

    @Autowired
    public InitialController(
            Decoder decoder, Encoder encoder, Client client) {
        this.initialControllerService = FeignClientFactory.getFeignClient(
                decoder, encoder, client,
                InitialControllerService.class);
    }

    @GetMapping(value = "/user/info")
    public JSONObject defaultLogin() {
        return initialControllerService.defaultLogin();
    }
}
