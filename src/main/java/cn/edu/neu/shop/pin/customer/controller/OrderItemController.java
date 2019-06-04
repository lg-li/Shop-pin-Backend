package cn.edu.neu.shop.pin.customer.controller;


import cn.edu.neu.shop.pin.customer.service.OrderItemService;
import cn.edu.neu.shop.pin.customer.service.security.UserService;
import cn.edu.neu.shop.pin.model.PinUser;
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
