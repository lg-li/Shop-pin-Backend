package cn.edu.neu.shop.pin.controller.super_admin;

import cn.edu.neu.shop.pin.mapper.PinSettingsConstantMapper;
import cn.edu.neu.shop.pin.model.PinSettingsConstant;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import java.util.List;

@RestController
@RequestMapping("/sadmin")
public class SAdminBannerController {
    @Autowired
    PinSettingsConstantMapper pinSettingsConstantMapper;

    @PostMapping("/save-banner")
    public JSONObject saveBanner(@RequestBody JSONArray request) {
        try {
            PinSettingsConstant pinSettingsConstant = new PinSettingsConstant();
            pinSettingsConstant.setConstantKey("banner_content");
            pinSettingsConstant.setConstantValue(request.toJSONString());

            pinSettingsConstantMapper.updateByPrimaryKey(pinSettingsConstant);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, "success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), "something wrong");
        }
    }

    @GetMapping("/get-banner")
    public JSONObject getBanner() {
        try {
            PinSettingsConstant pinSettingsConstant = pinSettingsConstantMapper.selectByPrimaryKey("banner_content");
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, pinSettingsConstant.getConstantValue());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), "something wrong");
        }
    }

}
