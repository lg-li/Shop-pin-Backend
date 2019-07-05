package cn.edu.neu.shop.pin.consumer.controller.commons;

import cn.edu.neu.shop.pin.consumer.factory.FeignClientFactory;
import cn.edu.neu.shop.pin.consumer.service.commons.PaymentControllerService;
import cn.edu.neu.shop.pin.consumer.service.commons.ProductCategoryControllerService;
import cn.edu.neu.shop.pin.consumer.service.commons.ProductControllerService;
import com.alibaba.fastjson.JSONObject;
import feign.Client;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Import(FeignClientsConfiguration.class)
@RestController
@RequestMapping("/commons/product")
public class ProductController {
    private ProductControllerService productControllerService;

    @Autowired
    public ProductController(
            Decoder decoder, Encoder encoder, Client client) {
        this.productControllerService = FeignClientFactory.getFeignClient(
                decoder, encoder, client,
                ProductControllerService.class);
    }

    @GetMapping("/by-category/{categoryId}/{pageNum}/{pageSize}")
    public JSONObject geProductByCategoryId(@PathVariable(value = "categoryId") Integer categoryId, @PathVariable(value = "pageNum") Integer pageNum, @PathVariable(value = "pageSize") Integer pageSize) {
        return productControllerService.geProductByCategoryId(categoryId, pageNum, pageSize);
    }

    @GetMapping("/{productId}")
    public JSONObject getProductById(@PathVariable(value = "productId") Integer productId) {
        return productControllerService.getProductById(productId);
    }

    @GetMapping("/{productId}/comment/{pageNum}/{pageSize}")
    public JSONObject getCommentByProductIdByPage(@PathVariable(value = "productId") Integer productId, @PathVariable(value = "pageNum") Integer pageNum, @PathVariable(value = "pageSize") Integer pageSize) {
        return productControllerService.getCommentByProductIdByPage(productId, pageNum, pageSize);
    }

    @GetMapping(value = "/hot/{pageNum}/{pageSize}")
    public JSONObject getHotProducts(@PathVariable(value = "pageNum") Integer pageNum, @PathVariable(value = "pageSize") Integer pageSize) {
        return productControllerService.getHotProducts(pageNum, pageSize);
    }

    @GetMapping(value = "/new/{pageNum}/{pageSize}")
    public JSONObject getNewProducts(@PathVariable(value = "pageNum") Integer pageNum, @PathVariable(value = "pageSize") Integer pageSize) {
        return productControllerService.getNewProducts(pageNum, pageSize);
    }

    @GetMapping(value = "/{productId}/is-collected")
    public JSONObject isCollected(@PathVariable(value = "productId") Integer productId) {
        return productControllerService.isCollected(productId);
    }

    @PostMapping("/create-visit-record")
    public JSONObject createVisitRecord(@RequestBody JSONObject requestJSON) {
        return productControllerService.createVisitRecord(requestJSON);
    }

    @PostMapping("/get-product-average-score")
    public JSONObject returnPraise(@RequestBody JSONObject request){
        return productControllerService.returnPraise(request);
    }
}
