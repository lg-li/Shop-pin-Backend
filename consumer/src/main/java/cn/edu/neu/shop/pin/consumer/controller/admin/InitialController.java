package cn.edu.neu.shop.pin.consumer.controller.admin;

import cn.edu.neu.shop.pin.consumer.service.admin.InitialControllerService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/manager")
public class InitialController {
    @Autowired
    InitialControllerService initialControllerService;

    @GetMapping(value = "/user/info")
    public JSONObject defaultLogin() {
        return initialControllerService.defaultLogin();
    }
}
