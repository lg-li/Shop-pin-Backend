package cn.edu.neu.shop.pin.controller.admin;

import cn.edu.neu.shop.pin.model.PinStore;
import cn.edu.neu.shop.pin.service.StoreService;
import cn.edu.neu.shop.pin.service.security.UserService;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/manager/store")
public class AdminStoreController {

    @Autowired
    UserService userService;
    @Autowired
    StoreService storeService;

    /**
     * 得到这个商人所有的商铺
     * @param req  传入的request
     * @return  返回所有的商铺
     */
    @GetMapping("/storeList")
    public JSONObject getProducts(HttpServletRequest req) {
        try {
            PinUser user = userService.whoAmI(req);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, storeService.getStoreListByOwnerId(user.getId()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    /**
     * 新增店铺
     */
    @PostMapping("/storeInfo")
    public JSONObject addStore(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        try{
            PinUser user = userService.whoAmI(httpServletRequest);
            String storeName = requestJSON.getString("name");
            String description = requestJSON.getString("description");
            String phone = requestJSON.getString("phone");
            String email = requestJSON.getString("email");
            String logoUrl = requestJSON.getString("logoUrl");
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS,
                    storeService.addStore(storeName, description, phone, email, logoUrl, user.getId()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

}
