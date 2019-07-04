package cn.edu.neu.shop.pin.controller.super_admin;

import cn.edu.neu.shop.pin.mapper.PinSettingsConstantMapper;
import cn.edu.neu.shop.pin.model.PinSettingsConstant;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.web.json.Json;

@RestController
@RequestMapping("/sadmin")
public class SAdminBannerController {
    @Autowired
    PinSettingsConstantMapper pinSettingsConstantMapper;

    @PostMapping("/save-banner")
    public JSONObject saveBanner(@RequestBody JSONObject request) {
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

}
