package cn.edu.neu.shop.pin.consumer.service.admin;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Service
@FeignClient(value = "Pin-Provider")
public interface AdminProductControllerService {
    @RequestMapping(value = "/goods/goods-list", method = RequestMethod.POST)
    public JSONObject getProducts(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/goods/goods-category", method = RequestMethod.GET)
    public JSONObject getProductFromSameStore(HttpServletRequest httpServletRequest);

    @RequestMapping(value = "/goods/category-list", method = RequestMethod.GET)
    public JSONObject getProductCatrgory();

    @RequestMapping(value = "/goods/update-category", method = RequestMethod.PUT)
    public JSONObject updateProductCategory(@RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/goods/create-product", method = RequestMethod.POST)
    public JSONObject createProduct(HttpServletRequest req, @RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/goods/create-sku-definition", method = RequestMethod.POST)
    public JSONObject createSkuDefinition(@RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/goods/create-sku", method = RequestMethod.POST)
    public JSONObject createSku(@RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/goods/is-shown", method = RequestMethod.PUT)
    public JSONObject updateProductIsShownStatus(@RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/goods/is-not-shown", method = RequestMethod.PUT)
    public JSONObject updateProductIsNotShownStatus(@RequestBody JSONObject requestJSON);
}
