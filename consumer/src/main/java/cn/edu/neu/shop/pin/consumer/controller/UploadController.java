package cn.edu.neu.shop.pin.consumer.controller;

import cn.edu.neu.shop.pin.consumer.factory.FeignClientFactory;
import cn.edu.neu.shop.pin.consumer.service.SignInControllerService;
import cn.edu.neu.shop.pin.consumer.service.SignUpControllerService;
import cn.edu.neu.shop.pin.consumer.service.UploadControllerService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Import(FeignClientsConfiguration.class)
@RestController
@RequestMapping(value = "/commons/upload")
public class UploadController {
    private UploadControllerService uploadControllerService;

    @Autowired
    public UploadController(
            Decoder decoder, Encoder encoder, Client client) {
        this.uploadControllerService = FeignClientFactory.getFeignClient(
                decoder, encoder, client,
                UploadControllerService.class);
    }

    @PostMapping("/image/base64")
    public JSONObject uploadStoreInfo(@RequestBody JSONObject uploadingInfo) {
        return uploadControllerService.uploadStoreInfo(uploadingInfo);
    }
}
