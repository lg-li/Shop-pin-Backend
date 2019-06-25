package cn.edu.neu.shop.pin.consumer.controller.admin;

import cn.edu.neu.shop.pin.consumer.service.admin.AdminStoreControllerService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/manager/store")
public class AdminStoreController {
    @Autowired
    AdminStoreControllerService adminStoreControllerService;
    @GetMapping("/storeList")
    public JSONObject getProducts(HttpServletRequest req){
        return adminStoreControllerService.getProducts(req);
    }

    @PostMapping("/storeInfo")
    public JSONObject addStoreInfo(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON){
        return adminStoreControllerService.addStoreInfo(httpServletRequest, requestJSON);
    }

    @PutMapping("/storeInfo")
    public JSONObject updateStoreInfo(@RequestBody JSONObject requestJSON){
        return adminStoreControllerService.updateStoreInfo(requestJSON);
    }

    @GetMapping("/close-batch")
    public JSONObject getGruopCloseBatchTime(HttpServletRequest httpServletRequest){
        return adminStoreControllerService.getGruopCloseBatchTime(httpServletRequest);
    }

    @PostMapping("/upload")
    public ResponseEntity<JSONObject> uploadStoreInfo(@RequestBody JSONObject uploadingInfo){
        return adminStoreControllerService.uploadStoreImage(uploadingInfo);
    }

    @DeleteMapping("/close-batch")
    public JSONObject deleteGroupCloseBatchTime(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON){
        return adminStoreControllerService.deleteGroupCloseBatchTime(httpServletRequest, requestJSON);
    }

    @PostMapping("/close-batch")
    public JSONObject addGroupCloseBatchTime(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON){
        return adminStoreControllerService.addGroupCloseBatchTime(httpServletRequest, requestJSON);
    }
}
