package cn.edu.neu.shop.pin.consumer.service.commons;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Service
public interface ProductControllerService {
    @RequestMapping(value = "/commons/product/by-category/{categoryId}/{pageNum}/{pageSize}", method = RequestMethod.GET)
    public JSONObject geProductByCategoryId(@PathVariable(value = "categoryId") Integer categoryId, @PathVariable(value = "pageNum") Integer pageNum, @PathVariable(value = "pageSize") Integer pageSize);

    @RequestMapping(value = "/commons/product/{productId}", method = RequestMethod.GET)
    public JSONObject getProductById(@PathVariable(value = "productId") Integer productId);

    @RequestMapping(value = "/commons/product/{productId}/comment/{pageNum}/{pageSize}", method = RequestMethod.GET)
    public JSONObject getCommentByProductIdByPage(@PathVariable(value = "productId") Integer productId, @PathVariable(value = "pageNum") Integer pageNum, @PathVariable(value = "pageSize") Integer pageSize);

    @RequestMapping(value = "/commons/product/hot/{pageNum}/{pageSize}", method = RequestMethod.GET)
    public JSONObject getHotProducts(@PathVariable(value = "pageNum") Integer pageNum, @PathVariable(value = "pageSize") Integer pageSize);

    @RequestMapping(value = "/commons/product/new/{pageNum}/{pageSize}", method = RequestMethod.GET)
    public JSONObject getNewProducts(@PathVariable(value = "pageNum") Integer pageNum, @PathVariable(value = "pageSize") Integer pageSize);

    @RequestMapping(value = "/commons/product/{productId}/is-collected", method = RequestMethod.GET)
    public JSONObject isCollected(@PathVariable(value = "productId") Integer productId);

    @RequestMapping(value = "/commons/product/create-visit-record", method = RequestMethod.POST)
    public JSONObject createVisitRecord(@RequestBody JSONObject requestJSON);
}
