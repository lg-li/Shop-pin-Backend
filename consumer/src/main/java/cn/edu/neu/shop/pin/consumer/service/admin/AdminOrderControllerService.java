package cn.edu.neu.shop.pin.consumer.service.admin;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Service
@FeignClient(value = "Pin-Provider")
public interface AdminOrderControllerService {
    @RequestMapping(value = "/admin/order/deliverNameList", method = RequestMethod.GET)
    JSONObject getExpressInfo();

    @RequestMapping(value = "/admin/order/query", method = RequestMethod.POST)
    JSONObject getOrderByCondition(@RequestBody JSONObject queryType);

    @RequestMapping(value = "/admin/order/get-group-order-list", method = RequestMethod.POST)
    JSONObject getGroupOrderByCondition(@RequestBody JSONObject queryType);

    @RequestMapping(value = "/admin/order/deliver-product", method = RequestMethod.PUT)
    JSONObject updateProductStatueToShip(@RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/admin/order/order-remark", method = RequestMethod.PUT)
    JSONObject updateMerchantRemark(@RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/admin/discount-setting", method = RequestMethod.POST)
    JSONObject discountSetting(@RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/admin/refund-order", method = RequestMethod.POST)
    JSONObject refundOrder(@RequestBody JSONObject requestJSON);
}
