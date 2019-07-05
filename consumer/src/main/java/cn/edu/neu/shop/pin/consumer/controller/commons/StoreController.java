package cn.edu.neu.shop.pin.consumer.controller.commons;

import cn.edu.neu.shop.pin.consumer.factory.FeignClientFactory;
import cn.edu.neu.shop.pin.consumer.service.commons.ProductControllerService;
import cn.edu.neu.shop.pin.consumer.service.commons.StoreControllerService;
import com.alibaba.fastjson.JSONObject;
import feign.Client;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Import(FeignClientsConfiguration.class)
@RestController
@RequestMapping("/commons/store")
public class StoreController {
    private StoreControllerService storeControllerService;

    @Autowired
    public StoreController(
            Decoder decoder, Encoder encoder, Client client) {
        this.storeControllerService = FeignClientFactory.getFeignClient(
                decoder, encoder, client,
                StoreControllerService.class);
    }

    @GetMapping("/{storeId}/products/{pageNum}/{pageSize}")
    public JSONObject getProductInfoByStoreId(@PathVariable(value = "storeId") Integer storeId, @PathVariable(value = "pageNum") Integer pageNum, @PathVariable(value = "pageSize") Integer pageSize) {
        return storeControllerService.getProductInfoByStoreId(storeId, pageNum, pageSize);
    }

    @GetMapping("/{storeId}")
    public JSONObject getStoreInfoByStoreId(@PathVariable Integer storeId) {
        return storeControllerService.getStoreInfoByStoreId(storeId);
    }
}
