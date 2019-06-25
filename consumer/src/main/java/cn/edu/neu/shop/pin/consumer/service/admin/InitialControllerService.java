package cn.edu.neu.shop.pin.consumer.service.admin;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Service
@FeignClient(value = "Pin-Provider")
public interface InitialControllerService {
    @RequestMapping(value = "/manager/user/info", method = RequestMethod.GET)
    JSONObject defaultLogin();
}
