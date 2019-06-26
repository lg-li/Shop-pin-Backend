package cn.edu.neu.shop.pin.consumer.service.security;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Service
public interface UserControllerService {
    @RequestMapping(value = "/user/sign-in", method = RequestMethod.GET)
    public String signIn(@RequestParam(value = "id") String id, @RequestParam(value = "password") String password, @PathVariable("UserController.signIn") String parameter);

    @RequestMapping(value = "/user/sign-up", method = RequestMethod.POST)
    public String signUp(/*@RequestBody PinUser user,*/ @PathVariable("UserController.signUpAndGetToken") String parameter);

    @RequestMapping(value = "/user/delete/{id}", method = RequestMethod.DELETE)
    public Integer delete(@PathVariable Integer id, @PathVariable("UserController.delete") String parameter);

    @RequestMapping(value = "/user/get", method = RequestMethod.GET)
    public Object search(@RequestParam(value = "id") Integer id, @PathVariable("UserController.search") String parameter);

    @RequestMapping(value = "/user/me", method = RequestMethod.GET)
    public /*UserResponseDTO*/JSONObject whoAmI();

    @RequestMapping(value = "/user/refresh", method = RequestMethod.GET)
    public String refresh();
}
