package cn.edu.neu.shop.pin.customer.controller;

import cn.edu.neu.shop.pin.customer.service.AddressService;
import cn.edu.neu.shop.pin.customer.service.security.UserService;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/commons/user")
public class UsersController {

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    /**
     * 根据用户ID，查询该用户的所有收获地址
     */
    @GetMapping("/address")
    public JSONObject getAddressByUserId(HttpServletRequest httpServletRequest){
        try{
            PinUser user = userService.whoAmI(httpServletRequest);
            JSONObject data = new JSONObject();
            data.put("list", addressService.getAllAddressesByUserId(user.getId()));
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    @PostMapping("/address")
    public JSONObject createAddress(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON){
        try{
            PinUser user = userService.whoAmI(httpServletRequest);
            String realName = requestJSON.getString("realName");
            String phone = requestJSON.getString("phone");
            String province = requestJSON.getString("province");
            String city = requestJSON.getString("city");
            String district = requestJSON.getString("district");
            String detail = requestJSON.getString("detail");
            Integer postCode = requestJSON.getInteger("postCode");
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS,
                    addressService.createAddressByUserId(user.getId(), realName, phone, province, city, district, detail, postCode));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    @PostMapping("/order")
    public JSONObject createOrderIndividual(HttpServletRequest httpServletRequest, @RequestBody JSONArray requestJSON){
//        JSONArray jsonArray = (JSONArray) requestJSON.get(0);
        boolean isSameStore = true;

        for(int i = 0; i < requestJSON.size(); i++) {
            JSONObject obj = requestJSON.getJSONObject(i);
        }
    }
}
