package cn.edu.neu.shop.pin.consumer.controller.admin;

import cn.edu.neu.shop.pin.consumer.service.admin.AdminCommentControllerService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Import(FeignClientsConfiguration.class)
@RestController
public class AdminCommentController {

    private AdminCommentControllerService adminCommentControllerService;

    @Autowired
    public AdminCommentController(
            Decoder decoder, Encoder encoder, Client client) {
        this.adminCommentControllerService = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .target(AdminCommentControllerService.class, "http://Pin-Provider");
    }

    @PostMapping("/comment/goods-comment")
    public JSONObject getCommentByProductId(@RequestBody JSONObject requestJSON) {
        return adminCommentControllerService.getCommentByProductId(requestJSON);
    }

    @GetMapping("/goods/goods-with-comment")
    public JSONObject getAllProductWithComment() {
        return adminCommentControllerService.getAllProductWithComment();
    }

    @PostMapping("/comment/reply-comment")
    public JSONObject updateMerchantReplyComment(@RequestBody JSONObject requestJSON) {
        return adminCommentControllerService.updateMerchantReplyComment(requestJSON);
    }
}
