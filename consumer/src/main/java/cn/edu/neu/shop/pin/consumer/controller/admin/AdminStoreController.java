package cn.edu.neu.shop.pin.consumer.controller.admin;

import cn.edu.neu.shop.pin.consumer.service.admin.AdminStoreControllerService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/manager/store")
public class AdminStoreController {
    @Autowired
    AdminStoreControllerService adminStoreControllerService;

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
    public ResponseEntity<JSONObject> uploadStoreInfo(@RequestBody JSONObject uploadingInfo) {
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
