package cn.edu.neu.shop.pin.consumer.controller.commons;

import cn.edu.neu.shop.pin.consumer.factory.FeignClientFactory;
import cn.edu.neu.shop.pin.consumer.service.commons.UserAddressControllerService;
import cn.edu.neu.shop.pin.consumer.service.commons.UserBasicInfoControllerService;
import com.alibaba.fastjson.JSONObject;
import feign.Client;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Import(FeignClientsConfiguration.class)
@RestController
@RequestMapping("/commons/user")
public class UserBasicInfoController {
    private UserBasicInfoControllerService userBasicInfoControllerService;

    @Autowired
    public UserBasicInfoController(
            Decoder decoder, Encoder encoder, Client client) {
        this.userBasicInfoControllerService = FeignClientFactory.getFeignClient(
                decoder, encoder, client,
                UserBasicInfoControllerService.class);
    }

    @GetMapping("/info")
    public JSONObject getUserInfo() {
        return userBasicInfoControllerService.getUserInfo();
    }

    @GetMapping("/product-visit-record")
    public JSONObject getUserProductRecord() {
        return userBasicInfoControllerService.getUserProductRecord();
    }

    @GetMapping("/check-in")
    public JSONObject checkIn() {
        return userBasicInfoControllerService.checkIn();
    }

    @GetMapping("/credit-record")
    public JSONObject getUserCreditData() {
        return userBasicInfoControllerService.getUserCreditData();
    }

    @GetMapping("/has-checked-in")
    public JSONObject hasCheckedIn() {
        return userBasicInfoControllerService.hasCheckedIn();
    }

    @PostMapping("/add-comment")
    public JSONObject addComment(@RequestBody JSONObject requestJSON) {
        return userBasicInfoControllerService.addComment(requestJSON);
    }

    @PostMapping("/update-phone")
    public JSONObject updatePhone(@RequestBody JSONObject requestJSON) {
        return userBasicInfoControllerService.updatePhone(requestJSON);
    }

    @PostMapping("/update-email")
    public JSONObject updateEmail(@RequestBody JSONObject requestJSON) {
        return userBasicInfoControllerService.updateEmail(requestJSON);
    }

    @PostMapping("/update-password")
    public JSONObject updatePassword(@RequestBody JSONObject requestJSON) {
        return userBasicInfoControllerService.updatePassword(requestJSON);
    }

    @PostMapping("/update-avatar-url")
    public JSONObject updateAvatarUrl(@RequestBody JSONObject requestJSON) {
        return userBasicInfoControllerService.updateAvatarUrl(requestJSON);
    }

    @PostMapping("/update-common-user-info")
    public JSONObject updateCommonUserInfo(@RequestBody JSONObject requestJSON) {
        return userBasicInfoControllerService.updateCommonUserInfo(requestJSON);
    }
}
