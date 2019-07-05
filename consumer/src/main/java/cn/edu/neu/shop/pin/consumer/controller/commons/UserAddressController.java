package cn.edu.neu.shop.pin.consumer.controller.commons;

import cn.edu.neu.shop.pin.consumer.factory.FeignClientFactory;
import cn.edu.neu.shop.pin.consumer.service.commons.ProductControllerService;
import cn.edu.neu.shop.pin.consumer.service.commons.StoreControllerService;
import cn.edu.neu.shop.pin.consumer.service.commons.UserAddressControllerService;
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
public class UserAddressController {
    private UserAddressControllerService userAddressControllerService;

    @Autowired
    public UserAddressController(
            Decoder decoder, Encoder encoder, Client client) {
        this.userAddressControllerService = FeignClientFactory.getFeignClient(
                decoder, encoder, client,
                UserAddressControllerService.class);
    }

    @GetMapping("/default-address")
    public JSONObject getDefaultAddress() {
        return userAddressControllerService.getDefaultAddress();
    }

    @GetMapping("/address")
    public JSONObject getAllAddresses() {
        return userAddressControllerService.getAllAddresses();
    }

    @PostMapping("/address")
    public JSONObject createAddress(@RequestBody JSONObject requestJSON) {
        return userAddressControllerService.createAddress(requestJSON);
    }

    @DeleteMapping("/address")
    public JSONObject deleteAddress(@RequestBody JSONObject requestJSON) {
        return userAddressControllerService.deleteAddress(requestJSON);
    }

    @PutMapping("/address")
    public JSONObject updateAddress(@RequestBody JSONObject requestJSON) {
        return userAddressControllerService.updateAddress(requestJSON);
    }
}
