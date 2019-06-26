package cn.edu.neu.shop.pin.consumer.controller.commons;

import cn.edu.neu.shop.pin.consumer.service.admin.InitialControllerService;
import cn.edu.neu.shop.pin.consumer.service.commons.OrderGroupControllerService;
import com.alibaba.fastjson.JSONObject;
import feign.Client;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.*;

@Import(FeignClientsConfiguration.class)
@RestController
@RequestMapping(value = "/commons/order-group")
public class OrderGroupController {
    private OrderGroupControllerService orderGroupControllerService;

    @Autowired
    public OrderGroupController(
            Decoder decoder, Encoder encoder, Client client) {
        this.orderGroupControllerService = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .target(OrderGroupControllerService.class, "http://Pin-Provider");
    }

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
