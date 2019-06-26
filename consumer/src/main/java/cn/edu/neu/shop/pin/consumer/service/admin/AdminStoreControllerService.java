package cn.edu.neu.shop.pin.consumer.service.admin;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Service
//@FeignClient(value = "Pin-Provider")
public interface AdminStoreControllerService {
    @RequestMapping(value = "/manager/store/storeList", method = RequestMethod.GET)
    JSONObject getProducts();

    @RequestMapping(value = "/manager/store/storeInfo", method = RequestMethod.POST)
    JSONObject addStoreInfo(@RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/manager/store/storeInfo", method = RequestMethod.PUT)
    JSONObject updateStoreInfo(@RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/manager/store/close-batch", method = RequestMethod.GET)
    JSONObject getGroupCloseBatchTime();

    @RequestMapping(value = "/manager/store/upload", method = RequestMethod.POST)
    ResponseEntity<JSONObject> uploadStoreImage(@RequestBody JSONObject uploadingInfo);

    @RequestMapping(value = "/manager/store/close-batch", method = RequestMethod.DELETE)
    JSONObject deleteGroupCloseBatchTime(@RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/manager/store/close-batch", method = RequestMethod.POST)
    JSONObject addGroupCloseBatchTime(@RequestBody JSONObject requestJSON);
}
