package cn.edu.neu.shop.pin.consumer.controller.commons;

import cn.edu.neu.shop.pin.consumer.service.commons.OrderGroupControllerService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/commons/order-group")
public class OrderGroupController {
    @Autowired
    OrderGroupControllerService orderGroupControllerService;

    @PostMapping("/create")
    public JSONObject createOrderGroup(HttpServletRequest httpServletRequest, @RequestBody JSONObject jsonObject) {
        return orderGroupControllerService.createOrderGroup(httpServletRequest, jsonObject);
    }

    @PostMapping("/join")
    public JSONObject joinOrderGroup(HttpServletRequest httpServletRequest, @RequestBody JSONObject jsonObject) {
        return orderGroupControllerService.joinOrderGroup(httpServletRequest, jsonObject);
    }

    @PostMapping("/quit")
    public JSONObject quitOrderGroup(HttpServletRequest httpServletRequest, @RequestBody JSONObject jsonObject) {
        return orderGroupControllerService.quitOrderGroup(httpServletRequest, jsonObject);
    }

    @GetMapping("/by-order-group-id/{orderGroupId}")
    public JSONObject getOrderGroupByOrderGroupId(@PathVariable(value = "orderGroupId") Integer orderGroupId) {
        return orderGroupControllerService.getOrderGroupByOrderGroupId(orderGroupId);
    }

    @GetMapping("/by-store-id/{storeId}")
    public JSONObject getTopTenOrderGroupsByStoreId(@PathVariable Integer storeId) {
        return orderGroupControllerService.getTopTenOrderGroupsByStoreId(storeId);
    }
}
