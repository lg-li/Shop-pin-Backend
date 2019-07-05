package cn.edu.neu.shop.pin.consumer.controller.security;

import cn.edu.neu.shop.pin.consumer.factory.FeignClientFactory;
import cn.edu.neu.shop.pin.consumer.service.commons.UserAddressControllerService;
import cn.edu.neu.shop.pin.consumer.service.commons.UserCollectionControllerService;
import cn.edu.neu.shop.pin.consumer.service.security.UserControllerService;
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

/**
 * 这个类估计是没有什么用的
 */
@Import(FeignClientsConfiguration.class)
@RestController
@RequestMapping("/user")
public class UserController {
    private UserControllerService userControllerService;

    @Autowired
    public UserController(
            Decoder decoder, Encoder encoder, Client client) {
        this.userControllerService = FeignClientFactory.getFeignClient(
                decoder, encoder, client,
                UserControllerService.class);
    }

    @GetMapping("/sign-in")
    public String signIn(@RequestParam(value = "id") String id, @RequestParam(value = "password") String password, @PathVariable("UserController.signIn") String parameter) {
        return userControllerService.signIn(id, password, parameter);
    }

    @PostMapping("/sign-up")
    public String signUp(/*@RequestBody PinUser user,*/ @PathVariable("UserController.signUpAndGetToken") String parameter) {
        return userControllerService.signUp(parameter);
    }

    @DeleteMapping(value = "/delete/{id}")
    public Integer delete(@PathVariable Integer id, @PathVariable("UserController.delete") String parameter) {
        return userControllerService.delete(id, parameter);
    }

    @GetMapping(value = "/get")
    public Object search(@RequestParam(value = "id") Integer id, @PathVariable("UserController.search") String parameter) {
        return userControllerService.search(id, parameter);
    }

    @GetMapping(value = "/me")
    public /*UserResponseDTO*/JSONObject whoAmI() {
        return userControllerService.whoAmI();
    }

    @GetMapping("/refresh")
    public String refresh() {
        return userControllerService.refresh();
    }
}
