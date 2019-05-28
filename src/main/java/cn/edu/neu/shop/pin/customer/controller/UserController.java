package cn.edu.neu.shop.pin.customer.controller;

import cn.edu.neu.shop.pin.customer.service.AddressService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commons/user")
public class UserController {

    @Autowired
    private AddressService addressService;

    /**
     * 根据用户ID，查询该用户的所有收获地址
     * @param userId
     * @return
     */
    @GetMapping("/{userId}/user-address")
    public JSONObject getAddressByUserId(@PathVariable(value = "userId") Integer userId){
        
        try{
            JSONObject data = new JSONObject();
            data.put("list", addressService.getAllAddressesByUserId(userId));
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }
}
