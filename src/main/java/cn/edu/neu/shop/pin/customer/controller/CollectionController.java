package cn.edu.neu.shop.pin.customer.controller;

import cn.edu.neu.shop.pin.customer.service.UserProductCollectionService;
import cn.edu.neu.shop.pin.customer.service.UserStoreCollectionService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author flyhero
 */

@RestController
@RequestMapping("/commons/collection")
public class CollectionController {

    @Autowired
    private UserProductCollectionService userProductCollectionService;

    @Autowired
    private UserStoreCollectionService userStoreCollectionService;

    /**
     * 获取商品收藏product-collection
     * @param userId
     * @return
     */
    @GetMapping("/user-product-collection/{userId}")
    public JSONObject getUserProductCollection(@PathVariable(value = "userId") Integer userId) {
        try {
            JSONObject data = new JSONObject();
            data.put("list", userProductCollectionService.getUserProductCollection(userId));
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    /**
     * 获取店铺收藏store-collection
     * @param userId
     * @return
     */
    @GetMapping("/user-store-collection/{userId}")
    public JSONObject getUserStoreCollection(@PathVariable(value = "userId") Integer userId) {
        try {
            JSONObject data = new JSONObject();
            data.put("list", userStoreCollectionService.getUserStoreCollection(userId));
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }
}
