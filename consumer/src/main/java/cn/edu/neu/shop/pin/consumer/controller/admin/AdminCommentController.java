package cn.edu.neu.shop.pin.consumer.controller.admin;

import cn.edu.neu.shop.pin.consumer.service.admin.AdminCommentControllerService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AdminCommentController {
    @Autowired
    AdminCommentControllerService adminCommentControllerService;

    @PostMapping("/comment/goods-comment")
    public JSONObject getCommentByProductId(@RequestBody JSONObject requestJSON) {
        return adminCommentControllerService.getCommentByProductId(requestJSON);
    }

    @GetMapping("/goods/goods-with-comment")
    public JSONObject getAllProductWithComment(HttpServletRequest httpServletRequest) {
        return adminCommentControllerService.getAllProductWithComment(httpServletRequest);
    }

    @PostMapping("/comment/reply-comment")
    public JSONObject updateMerchantReplyComment(@RequestBody JSONObject requestJSON) {
        return adminCommentControllerService.updateMerchantReplyComment(requestJSON);
    }
}
