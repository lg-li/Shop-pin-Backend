package cn.edu.neu.shop.pin.consumer.controller.super_admin;

import cn.edu.neu.shop.pin.consumer.factory.FeignClientFactory;
import cn.edu.neu.shop.pin.consumer.service.super_admin.SAdminBannerControllerService;
import com.alibaba.fastjson.JSONObject;
import feign.Client;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Import(FeignClientsConfiguration.class)
@RestController
@RequestMapping(value = "/sadmin")
public class SAdminBannerController {
    private SAdminBannerControllerService sAdminBannerControllerService;

    @Autowired
    public SAdminBannerController(
            Decoder decoder, Encoder encoder, Client client) {
        this.sAdminBannerControllerService = FeignClientFactory.getFeignClient(
                decoder, encoder, client,
                SAdminBannerControllerService.class);
    }

    @PostMapping("/save-banner")
    public JSONObject saveBanner(JSONObject request) {
        return sAdminBannerControllerService.saveBanner(request);
    }

    @PostMapping("/get-banner")
    public JSONObject getBanner() { return sAdminBannerControllerService.getBanner();}
    
}
