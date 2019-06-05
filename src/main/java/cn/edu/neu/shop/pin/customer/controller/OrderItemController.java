package cn.edu.neu.shop.pin.customer.controller;


import cn.edu.neu.shop.pin.customer.service.OrderItemService;
import cn.edu.neu.shop.pin.customer.service.security.UserService;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.ResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.annotation.Order;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("commons/order-item")
public class OrderItemController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderItemService orderItemService;

    @PostMapping("/add")
    public JSONObject addOrderItem(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        try {
            PinUser user = userService.whoAmI(httpServletRequest);
            Integer productId = requestJSON.getInteger("productId");
            Integer skuId = requestJSON.getInteger("skuId");
            Integer amount = requestJSON.getInteger("amount");
            int code = orderItemService.addOrderItem(user.getId(), productId, skuId, amount);
            if(code == OrderItemService.STATUS_ADD_ORDER_ITEM_SUCCESS) {
                return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, null);
            }  else if(code == OrderItemService.STATUS_ADD_ORDER_ITEM_INVALID_ID) {
                return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, "添加购物车失败", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    @PostMapping("/add")
//    public JSONObject addProductToOrder(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
//        try{
//            PinUser user = userService.whoAmI(httpServletRequest);
//            Integer productAmount = requestJSON.getInteger("productAmount");
//
//        } catch (Exception e) {
//
//        }
//    }
}
