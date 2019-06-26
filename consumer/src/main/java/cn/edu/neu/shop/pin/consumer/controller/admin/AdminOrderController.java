package cn.edu.neu.shop.pin.consumer.controller.admin;

import cn.edu.neu.shop.pin.consumer.service.admin.AdminHomeControllerService;
import cn.edu.neu.shop.pin.consumer.service.admin.AdminOrderControllerService;
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
@RequestMapping("/admin")
public class AdminOrderController {
    private AdminOrderControllerService adminOrderControllerService;

    @Autowired
    public AdminOrderController(
            Decoder decoder, Encoder encoder, Client client) {
        this.adminOrderControllerService = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .target(AdminOrderControllerService.class, "http://Pin-Provider");
    }

    @GetMapping("/order/deliverNameList")
    public JSONObject getExpressInfo() {
        return adminOrderControllerService.getExpressInfo();
    }

    @PostMapping("/order/query")
    public JSONObject getOrderByCondition(@RequestBody JSONObject queryType) {
        return adminOrderControllerService.getOrderByCondition(queryType);
    }

    @PostMapping("/order/get-group-order-list")
    public JSONObject getGroupOrderByCondition(@RequestBody JSONObject queryType) {
        return adminOrderControllerService.getGroupOrderByCondition(queryType);
    }

    @PutMapping("/order/deliver-product")
    public JSONObject updateProductStatueToShip(@RequestBody JSONObject requestJSON) {
        return adminOrderControllerService.updateProductStatueToShip(requestJSON);
    }

    @PutMapping("/order/order-remark")
    public JSONObject updateMerchantRemark(@RequestBody JSONObject requestJSON) {
        return adminOrderControllerService.updateMerchantRemark(requestJSON);
    }

    @PostMapping("/discount-setting")
    public JSONObject discountSetting(@RequestBody JSONObject requestJSON) {
        return adminOrderControllerService.discountSetting(requestJSON);
    }

    @PostMapping("/refund-order")
    public JSONObject refundOrder(@RequestBody JSONObject requestJSON) {
        return adminOrderControllerService.refundOrder(requestJSON);
    }
}
