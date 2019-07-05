package cn.edu.neu.shop.pin.controller;

import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import cn.edu.neu.shop.pin.util.img.ImgUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/commons/upload")

public class UploadController {
    @PostMapping("/image/base64")
    public JSONObject uploadStoreInfo(@RequestBody JSONObject uploadingInfo) {
        String base64Img = uploadingInfo.getString("image");
        JSONObject data = new JSONObject();
        data.put("url",ImgUtil.upload(base64Img));
        return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
    }
}

