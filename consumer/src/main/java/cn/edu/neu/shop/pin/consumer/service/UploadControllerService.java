package cn.edu.neu.shop.pin.consumer.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Service
public interface UploadControllerService {
    @RequestMapping(value = "/commons/upload/image/base64", method = RequestMethod.POST)
    public JSONObject uploadStoreInfo(@RequestBody JSONObject uploadingInfo);
}
