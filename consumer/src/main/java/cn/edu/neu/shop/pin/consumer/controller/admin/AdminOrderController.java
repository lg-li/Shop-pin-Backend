package cn.edu.neu.shop.pin.consumer.controller.admin;

import cn.edu.neu.shop.pin.consumer.service.admin.AdminOrderControllerService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin")
public class AdminOrderController {
    @Autowired
    AdminOrderControllerService adminOrderControllerService;

    @GetMapping("/order/deliverNameList")
    public JSONObject getExpressInfo() {
        return adminOrderControllerService.getExpressInfo();
    }

    @PostMapping("/order/query")
    public JSONObject getOrderByCondition(HttpServletRequest req, @RequestBody JSONObject queryType) {
        return adminOrderControllerService.getOrderByCondition(req, queryType);
    }

    @PostMapping("/order/get-group-order-list")
    public JSONObject getGroupOrderByCondition(HttpServletRequest req, @RequestBody JSONObject queryType) {
        return adminOrderControllerService.getGroupOrderByCondition(req, queryType);
    }

    @PutMapping("/order/deliver-product")
    public JSONObject updateProductStatueToShip(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        return adminOrderControllerService.updateProductStatueToShip(httpServletRequest, requestJSON);
    }

    @PutMapping("/order/order-remark")
    public JSONObject updateMerchantRemark(@RequestBody JSONObject requestJSON) {
        return adminOrderControllerService.updateMerchantRemark(requestJSON);
    }

    @PostMapping("/discount-setting")
    public JSONObject discountSetting(@RequestBody JSONObject requestJSON) {
        return adminOrderControllerService.discountSetting(requestJSON);
    }
}
