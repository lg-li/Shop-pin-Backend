package cn.edu.neu.shop.pin.customer.controller.admin;

import cn.edu.neu.shop.pin.customer.service.StoreService;
import cn.edu.neu.shop.pin.customer.service.security.UserService;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public JSONObject getProducts(HttpServletRequest req) {
        try {
            PinUser user = userService.whoAmI(req);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, storeService.getStoreListByOwnerId(user.getId()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }
}
