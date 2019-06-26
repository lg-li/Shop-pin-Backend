package cn.edu.neu.shop.pin.consumer.service.commons;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Service
public interface UserCollectionControllerService {
    @RequestMapping(value = "/commons/user/product-collection", method = RequestMethod.GET)
    public JSONObject getUserProductCollection();

    @RequestMapping(value = "/commons/user/store-collection", method = RequestMethod.GET)
    public JSONObject getUserStoreCollection();

    @RequestMapping(value = "/commons/user/product-collection", method = RequestMethod.POST)
    public JSONObject addProductToCollection(@RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/commons/user/product-collection/{productId}", method = RequestMethod.DELETE)
    public JSONObject deleteUserProductCollection(@PathVariable Integer productId);

    @RequestMapping(value = "/commons/user/store-collection", method = RequestMethod.POST)
    public JSONObject addStoreToCollection(@RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/commons/user/store-collection/{storeId}", method = RequestMethod.DELETE)
    public JSONObject deleteUserStoreCollection(@PathVariable Integer storeId);
}
