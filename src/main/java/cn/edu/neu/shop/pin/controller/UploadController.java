package cn.edu.neu.shop.pin.controller;

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
    public ResponseEntity<JSONObject> uploadStoreInfo(@RequestBody JSONObject uploadingInfo){
        String base64Img = uploadingInfo.getString("image");
        return ImgUtil.upload(base64Img,"https://sm.ms/api/upload");
    }
}
