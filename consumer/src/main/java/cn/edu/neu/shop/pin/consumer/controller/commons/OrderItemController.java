package cn.edu.neu.shop.pin.consumer.controller.commons;

import cn.edu.neu.shop.pin.consumer.factory.FeignClientFactory;
import cn.edu.neu.shop.pin.consumer.service.admin.AdminStoreControllerService;
import cn.edu.neu.shop.pin.consumer.service.commons.OrderIndividualControllerService;
import cn.edu.neu.shop.pin.consumer.service.commons.OrderItemControllerService;
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

import javax.servlet.http.HttpServletRequest;

@Import(FeignClientsConfiguration.class)
@RestController
@RequestMapping("/commons/order")
public class OrderItemController {
    private OrderItemControllerService orderItemControllerService;

    @Autowired
    public OrderItemController(
            Decoder decoder, Encoder encoder, Client client) {
        this.orderItemControllerService = FeignClientFactory.getFeignClient(
                decoder, encoder, client,
                OrderItemControllerService.class);
    }

    @GetMapping("/order-items")
    public JSONObject getAllOrderItems() {
        return orderItemControllerService.getAllOrderItems();
    }

    @PostMapping("/order-item")
    public JSONObject addOrderItem(@RequestBody JSONObject requestJSON) {
        return orderItemControllerService.addOrderItem(requestJSON);
    }

    @DeleteMapping("/order-items")
    public JSONObject deleteOrderItems(@RequestBody JSONObject requestJSON) {
        return orderItemControllerService.deleteOrderItems(requestJSON);
    }

    @PostMapping("/order-item/change-amount")
    public JSONObject changeOrderItemAmount(@RequestBody JSONObject requestJSON) {
        return orderItemControllerService.changeOrderItemAmount(requestJSON);
    }

}
