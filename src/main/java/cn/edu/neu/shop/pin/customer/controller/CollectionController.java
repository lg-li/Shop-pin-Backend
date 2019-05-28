package cn.edu.neu.shop.pin.customer.controller;

import cn.edu.neu.shop.pin.customer.service.UserProductCollectionService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/commons/collection")
public class CollectionController {

    @Autowired
    private UserProductCollectionService userProductCollectionService;

    @GetMapping("/user-product-collection/{userId}")
    public JSONObject getUserProductCollection(@RequestBody JSONObject requestJSON) {
        try {
            Integer userId = requestJSON.getInteger("userId");
            JSONObject data = new JSONObject();
            data.put("list", userProductCollectionService.getUserProductCollection(userId));
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }
}
