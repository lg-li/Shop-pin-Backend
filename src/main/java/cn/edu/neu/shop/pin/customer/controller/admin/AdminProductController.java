package cn.edu.neu.shop.pin.customer.controller.admin;

import cn.edu.neu.shop.pin.customer.service.security.UserService;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/manager/product")
public class AdminProductController {
    @Autowired
    UserService userService;

    /**
     * TODO:返回不同存货类型的商品
     * @param req
     * @param queryType
     * @return
     */
    @GetMapping("/")
    public JSONObject getProducts(HttpServletRequest req, @RequestParam String queryType){
        try {
            PinUser user = userService.whoAmI(req);
            String currentStoreId = req.getHeader("Current-Store");

            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }
}
