package cn.edu.neu.shop.pin.customer.controller;

import cn.edu.neu.shop.pin.customer.service.ProductCategoryService;
import cn.edu.neu.shop.pin.customer.service.ProductInfoService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONObject;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/commons/product")
public class ProductController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductInfoService productInfoService;


    @PostMapping("/category/get-all-by-layer")
    public JSONObject getCategoryByLayer(@RequestBody JSONObject requestJSON) {
        try{
            Integer layer = requestJSON.getInteger("layer");
            JSONObject data = new JSONObject();
            data.put("list", productCategoryService.getProductCategoryByLayer(layer));
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    @GetMapping("/{product}")
    public JSONObject getProductInfoByProductId(JSONObject requestJSON){
        try{
            Integer productId = requestJSON.getInteger("productId");
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, productInfoService.getProInfoByProId(productId));
        }catch(Exception e){
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }
}
