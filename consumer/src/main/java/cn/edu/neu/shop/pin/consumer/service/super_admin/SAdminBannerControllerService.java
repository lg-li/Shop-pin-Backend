package cn.edu.neu.shop.pin.consumer.service.super_admin;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface SAdminBannerControllerService {
    @RequestMapping(value = "/sadmin/save-banner", method = RequestMethod.POST)
    JSONObject saveBanner(JSONObject request);

    @RequestMapping(value = "/sadmin/get-banner", method = RequestMethod.GET)
    JSONObject getBanner();
}
