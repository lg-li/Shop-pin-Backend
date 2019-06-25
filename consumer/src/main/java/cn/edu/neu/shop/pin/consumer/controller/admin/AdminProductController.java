package cn.edu.neu.shop.pin.consumer.controller.admin;

import cn.edu.neu.shop.pin.consumer.service.admin.AdminProductControllerService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/goods")
public class AdminProductController {
    @Autowired
    AdminProductControllerService adminProductControllerService;

    @PostMapping("/goods-list")
    public JSONObject getProducts(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        return adminProductControllerService.getProducts(httpServletRequest, requestJSON);
    }

    @GetMapping("/goods-category")
    public JSONObject getProductFromSameStore(HttpServletRequest httpServletRequest) {
        return adminProductControllerService.getProductFromSameStore(httpServletRequest);
    }

    @GetMapping("/category-list")
    public JSONObject getProductCatrgory() {
        return adminProductControllerService.getProductCatrgory();
    }

    @PutMapping("/update-category")
    public JSONObject updateProductCategory(@RequestBody JSONObject requestJSON) {
        return adminProductControllerService.updateProductCategory(requestJSON);
    }

    @PostMapping("/create-product")
    public JSONObject createProduct(HttpServletRequest req, @RequestBody JSONObject requestJSON) {
        return adminProductControllerService.createProduct(req, requestJSON);
    }

    @PostMapping("/create-sku-definition")
    public JSONObject createSkuDefinition(@RequestBody JSONObject requestJSON) {
        return adminProductControllerService.createSkuDefinition(requestJSON);
    }

    @PostMapping("/create-sku")
    public JSONObject createSku(@RequestBody JSONObject requestJSON) {
        return adminProductControllerService.createSku(requestJSON);
    }

    @PutMapping("/is-shown")
    public JSONObject updateProductIsShownStatus(@RequestBody JSONObject requestJSON) {
        return adminProductControllerService.updateProductIsShownStatus(requestJSON);
    }

    @PutMapping("/is-not-shown")
    public JSONObject updateProductIsNotShownStatus(@RequestBody JSONObject requestJSON) {
        return adminProductControllerService.updateProductIsNotShownStatus(requestJSON);
    }
}
