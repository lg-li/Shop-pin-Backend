package cn.edu.neu.shop.pin.consumer.controller.admin;

import cn.edu.neu.shop.pin.consumer.factory.FeignClientFactory;
import cn.edu.neu.shop.pin.consumer.service.admin.AdminCommentControllerService;
import com.alibaba.fastjson.JSONObject;
import feign.Client;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
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
    public AdminCommentController(Decoder decoder, Encoder encoder, Client client) {
        this.adminCommentControllerService = FeignClientFactory.getFeignClient(
                decoder, encoder, client,
                AdminCommentControllerService.class);
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
