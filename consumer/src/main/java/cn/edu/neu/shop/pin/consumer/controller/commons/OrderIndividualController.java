package cn.edu.neu.shop.pin.consumer.controller.commons;

import cn.edu.neu.shop.pin.consumer.factory.FeignClientFactory;
import cn.edu.neu.shop.pin.consumer.service.commons.OrderIndividualControllerService;
import com.alibaba.fastjson.JSONObject;
import feign.Client;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.*;

@Import(FeignClientsConfiguration.class)
@RestController
@RequestMapping("/commons/order")
public class OrderIndividualController {
    private OrderIndividualControllerService orderIndividualControllerService;

    @Autowired
    public OrderIndividualController(
            Decoder decoder, Encoder encoder, Client client) {
        this.orderIndividualControllerService = FeignClientFactory.getFeignClient(
                decoder, encoder, client,
                OrderIndividualControllerService.class);
    }

    @PostMapping("/order-individual")
    public JSONObject createOrderIndividual(@RequestBody JSONObject requestObject) {
        return orderIndividualControllerService.createOrderIndividual(requestObject);
    }

    @GetMapping("/order-individual/recent")
    public JSONObject getRecentThreeMonthsOrderIndividuals() {
        return orderIndividualControllerService.getRecentThreeMonthsOrderIndividuals();
    }

    @PostMapping("/order-individual/confirm-receipt")
    public JSONObject confirmReceipt(@RequestBody JSONObject requestJSON) {
        return orderIndividualControllerService.confirmReceipt(requestJSON);
    }

    @PostMapping("/refund")
    public JSONObject refundOrder(@RequestBody JSONObject requestJSON) {
        return orderIndividualControllerService.refundOrder(requestJSON);
    }
}
