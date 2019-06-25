package cn.edu.neu.shop.pin.consumer.controller.admin;

import cn.edu.neu.shop.pin.consumer.service.admin.AdminHomeControllerService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminHomeController {
    @Autowired
    AdminHomeControllerService adminHomeControllerService;

    @GetMapping("/home")
    public JSONObject getCommentSevenDays() {
        return adminHomeControllerService.getCommentSevenDays();
    }
}
