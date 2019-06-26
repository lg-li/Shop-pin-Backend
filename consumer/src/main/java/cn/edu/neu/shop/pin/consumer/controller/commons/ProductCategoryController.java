package cn.edu.neu.shop.pin.consumer.controller.commons;

import cn.edu.neu.shop.pin.consumer.service.commons.PaymentControllerService;
import cn.edu.neu.shop.pin.consumer.service.commons.ProductCategoryControllerService;
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

@Import(FeignClientsConfiguration.class)
@RestController
@RequestMapping("/commons/product/category")
public class ProductCategoryController {
    private ProductCategoryControllerService productCategoryControllerService;

    @Autowired
    public ProductCategoryController(
            Decoder decoder, Encoder encoder, Client client) {
        this.productCategoryControllerService = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .target(ProductCategoryControllerService.class, "http://Pin-Provider");
    }

    @PostMapping("/get-all-by-layer")
    public JSONObject getCategoryByLayer(@RequestBody JSONObject requestJSON) {
        return productCategoryControllerService.getCategoryByLayer(requestJSON);
    }

    @GetMapping("/all")
    public JSONObject geAllCategory() {
        return productCategoryControllerService.geAllCategory();
    }
}
