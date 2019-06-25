package cn.edu.neu.shop.pin.consumer.service.admin;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Service
@FeignClient(value = "Pin-Provider")
public interface AdminProductControllerService {
    @RequestMapping(value = "/goods/goods-list", method = RequestMethod.POST)
    JSONObject getProducts(@RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/goods/goods-category", method = RequestMethod.GET)
    JSONObject getProductFromSameStore();

    @RequestMapping(value = "/goods/category-list", method = RequestMethod.GET)
    JSONObject getProductCatrgory();

    @RequestMapping(value = "/goods/update-category", method = RequestMethod.PUT)
    JSONObject updateProductCategory(@RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/goods/create-product", method = RequestMethod.POST)
    JSONObject createProduct(@RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/goods/create-sku-definition", method = RequestMethod.POST)
    JSONObject createSkuDefinition(@RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/goods/create-sku", method = RequestMethod.POST)
    JSONObject createSku(@RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/goods/is-shown", method = RequestMethod.PUT)
    JSONObject updateProductIsShownStatus(@RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/goods/is-not-shown", method = RequestMethod.PUT)
    JSONObject updateProductIsNotShownStatus(@RequestBody JSONObject requestJSON);
}
