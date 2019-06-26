package cn.edu.neu.shop.pin.consumer.service.commons;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Service
public interface UserAddressControllerService {
    @RequestMapping(value = "/commons/user/default-address", method = RequestMethod.GET)
    public JSONObject getDefaultAddress();

    @RequestMapping(value = "/commons/user/address", method = RequestMethod.GET)
    public JSONObject getAllAddresses();

    @RequestMapping(value = "/commons/user/address", method = RequestMethod.POST)
    public JSONObject createAddress(@RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/commons/user/address", method = RequestMethod.DELETE)
    public JSONObject deleteAddress(@RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/commons/user/address", method = RequestMethod.PUT)
    public JSONObject updateAddress(@RequestBody JSONObject requestJSON);
}
