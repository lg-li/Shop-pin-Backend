package cn.edu.neu.shop.pin.consumer.service.admin;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Service
@FeignClient(value = "Pin-Provider")
public interface AdminStoreControllerService {
    @RequestMapping(value = "/manager/store/storeList", method = RequestMethod.GET)
    public JSONObject getProducts(HttpServletRequest req);

    @RequestMapping(value = "/manager/store/storeInfo", method = RequestMethod.POST)
    public JSONObject addStoreInfo(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/manager/store/storeInfo", method = RequestMethod.PUT)
    public JSONObject updateStoreInfo(@RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/manager/store/close-batch", method = RequestMethod.GET)
    public JSONObject getGruopCloseBatchTime(HttpServletRequest httpServletRequest);

    @RequestMapping(value = "/manager/store/upload", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> uploadStoreImage(@RequestBody JSONObject uploadingInfo);

    @RequestMapping(value = "/manager/store/close-batch", method = RequestMethod.DELETE)
    public JSONObject deleteGroupCloseBatchTime(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/manager/store/close-batch", method = RequestMethod.POST)
    public JSONObject addGroupCloseBatchTime(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON);
}
