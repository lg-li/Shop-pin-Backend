package cn.edu.neu.shop.pin.consumer.controller.admin;

import cn.edu.neu.shop.pin.consumer.factory.FeignClientFactory;
import cn.edu.neu.shop.pin.consumer.service.admin.AdminHomeControllerService;
import cn.edu.neu.shop.pin.consumer.service.admin.AdminStoreControllerService;
import com.alibaba.fastjson.JSONObject;
import feign.Client;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Import(FeignClientsConfiguration.class)
@RestController
@RequestMapping(value = "/manager/store")
public class AdminStoreController {
    private AdminStoreControllerService adminStoreControllerService;

    @Autowired
    public AdminStoreController(
            Decoder decoder, Encoder encoder, Client client) {
        this.adminStoreControllerService = FeignClientFactory.getFeignClient(
                decoder, encoder, client,
                AdminStoreControllerService.class);
    }

    @GetMapping("/storeList")
    public JSONObject getProducts() {
        return adminStoreControllerService.getProducts();
    }

    @PostMapping("/storeInfo")
    public JSONObject addStoreInfo(@RequestBody JSONObject requestJSON) {
        return adminStoreControllerService.addStoreInfo(requestJSON);
    }

    @PutMapping("/storeInfo")
    public JSONObject updateStoreInfo(@RequestBody JSONObject requestJSON) {
        return adminStoreControllerService.updateStoreInfo(requestJSON);
    }

    @GetMapping("/close-batch")
    public JSONObject getGroupCloseBatchTime() {
        return adminStoreControllerService.getGroupCloseBatchTime();
    }

    @PostMapping("/upload")
    public JSONObject uploadStoreInfo(@RequestBody JSONObject uploadingInfo) {
        return adminStoreControllerService.uploadStoreImage(uploadingInfo);
    }

    @DeleteMapping("/close-batch")
    public JSONObject deleteGroupCloseBatchTime(@RequestBody JSONObject requestJSON) {
        return adminStoreControllerService.deleteGroupCloseBatchTime(requestJSON);
    }

    @PostMapping("/close-batch")
    public JSONObject addGroupCloseBatchTime(@RequestBody JSONObject requestJSON) {
        return adminStoreControllerService.addGroupCloseBatchTime(requestJSON);
    }
}
