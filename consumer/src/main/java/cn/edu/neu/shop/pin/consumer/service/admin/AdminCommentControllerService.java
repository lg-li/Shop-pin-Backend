package cn.edu.neu.shop.pin.consumer.service.admin;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Service
@FeignClient(value = "Pin-Provider")
public interface AdminCommentControllerService {
    @RequestMapping(value = "/comment/goods-comment", method = RequestMethod.POST)
    JSONObject getCommentByProductId(@RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/goods/goods-with-comment", method = RequestMethod.GET)
    JSONObject getAllProductWithComment();

    @RequestMapping(value = "/comment/reply-comment", method = RequestMethod.POST)
    JSONObject updateMerchantReplyComment(@RequestBody JSONObject requestJSON);
}
