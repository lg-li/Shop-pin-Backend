package cn.edu.neu.shop.pin.consumer.service.commons;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Service
public interface UserBasicInfoControllerService {
    @RequestMapping(value = "/commons/user/info", method = RequestMethod.GET)
    public JSONObject getUserInfo();

    @RequestMapping(value = "/commons/user/product-visit-record", method = RequestMethod.GET)
    public JSONObject getUserProductRecord();

    @RequestMapping(value = "/commons/user/check-in", method = RequestMethod.GET)
    public JSONObject checkIn();

    @RequestMapping(value = "/commons/user/credit-record", method = RequestMethod.GET)
    public JSONObject getUserCreditData();

    @RequestMapping(value = "/commons/user/has-checked-in", method = RequestMethod.GET)
    public JSONObject hasCheckedIn();

    @RequestMapping(value = "/commons/user/add-comment", method = RequestMethod.POST)
    public JSONObject addComment(@RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/commons/user/update-phone", method = RequestMethod.POST)
    public JSONObject updatePhone(@RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/commons/user/update-email", method = RequestMethod.POST)
    public JSONObject updateEmail(@RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/commons/user/update-password", method = RequestMethod.POST)
    public JSONObject updatePassword(@RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/commons/user/update-avatar-url", method = RequestMethod.POST)
    public JSONObject updateAvatarUrl(@RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/commons/user/update-common-user-info", method = RequestMethod.POST)
    public JSONObject updateCommonUserInfo(@RequestBody JSONObject requestJSON);
}
