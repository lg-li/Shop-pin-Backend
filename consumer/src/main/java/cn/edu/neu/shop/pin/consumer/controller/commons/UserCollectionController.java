package cn.edu.neu.shop.pin.consumer.controller.commons;

import cn.edu.neu.shop.pin.consumer.service.commons.StoreControllerService;
import cn.edu.neu.shop.pin.consumer.service.commons.UserCollectionControllerService;
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
@RequestMapping("/commons/user")
public class UserCollectionController {
    private UserCollectionControllerService userCollectionControllerService;

    @Autowired
    public UserCollectionController(
            Decoder decoder, Encoder encoder, Client client) {
        this.userCollectionControllerService = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .target(UserCollectionControllerService.class, "http://Pin-Provider");
    }

    @GetMapping("/product-collection")
    public JSONObject getUserProductCollection() {
        return userCollectionControllerService.getUserProductCollection();
    }

    @GetMapping("/store-collection")
    public JSONObject getUserStoreCollection() {
        return userCollectionControllerService.getUserStoreCollection();
    }

    @PostMapping("/product-collection")
    public JSONObject addProductToCollection(@RequestBody JSONObject requestJSON) {
        return userCollectionControllerService.addProductToCollection(requestJSON);
    }

    @DeleteMapping("/product-collection/{productId}")
    public JSONObject deleteUserProductCollection(@PathVariable Integer productId) {
        return userCollectionControllerService.deleteUserProductCollection(productId);
    }

    @PostMapping("/store-collection")
    public JSONObject addStoreToCollection(@RequestBody JSONObject requestJSON) {
        return userCollectionControllerService.addStoreToCollection(requestJSON);
    }

    @DeleteMapping("/store-collection/{storeId}")
    public JSONObject deleteUserStoreCollection(@PathVariable Integer storeId) {
        return userCollectionControllerService.deleteUserStoreCollection(storeId);
    }
}
