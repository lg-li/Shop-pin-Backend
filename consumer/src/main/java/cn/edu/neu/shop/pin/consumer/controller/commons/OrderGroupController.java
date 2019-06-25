package cn.edu.neu.shop.pin.consumer.controller.commons;

import cn.edu.neu.shop.pin.consumer.service.commons.OrderGroupControllerService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/commons/order-group")
public class OrderGroupController {
    @Autowired
    OrderGroupControllerService orderGroupControllerService;

    @PostMapping("/create")
    public JSONObject createOrderGroup(@RequestBody JSONObject jsonObject) {
        return orderGroupControllerService.createOrderGroup(jsonObject);
    }

    @PostMapping("/join")
    public JSONObject joinOrderGroup(@RequestBody JSONObject jsonObject) {
        return orderGroupControllerService.joinOrderGroup(jsonObject);
    }

    @PostMapping("/quit")
    public JSONObject quitOrderGroup(@RequestBody JSONObject jsonObject) {
        return orderGroupControllerService.quitOrderGroup(jsonObject);
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
