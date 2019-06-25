package cn.edu.neu.shop.pin.consumer.controller.admin;

import cn.edu.neu.shop.pin.consumer.service.admin.AdminProductControllerService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/goods")
public class AdminProductController {
    @Autowired
    AdminProductControllerService adminProductControllerService;

    @PostMapping("/goods-list")
    public JSONObject getProducts(@RequestBody JSONObject requestJSON) {
        return adminProductControllerService.getProducts(requestJSON);
    }

    @GetMapping("/goods-category")
    public JSONObject getProductFromSameStore() {
        return adminProductControllerService.getProductFromSameStore();
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
    public JSONObject createProduct(@RequestBody JSONObject requestJSON) {
        return adminProductControllerService.createProduct(requestJSON);
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
