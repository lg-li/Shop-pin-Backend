package cn.edu.neu.shop.pin.consumer.controller.commons;

import cn.edu.neu.shop.pin.consumer.factory.FeignClientFactory;
import cn.edu.neu.shop.pin.consumer.service.commons.PaymentControllerService;
import com.alibaba.fastjson.JSONObject;
import feign.Client;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Import(FeignClientsConfiguration.class)
@RestController
@RequestMapping(value = "/commons/payment")
public class PaymentController {
    private PaymentControllerService paymentControllerService;

    @Autowired
    public PaymentController(
            Decoder decoder, Encoder encoder, Client client) {
        this.paymentControllerService = FeignClientFactory.getFeignClient(
                decoder, encoder, client,
                PaymentControllerService.class);
    }

    @PostMapping(value = "/unified-pay")
    public JSONObject unifiedPay(@RequestBody JSONObject requestObject) {
        return paymentControllerService.unifiedPay(requestObject);
    }

}
