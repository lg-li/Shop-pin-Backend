package cn.edu.neu.shop.pin.consumer.service.admin;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Service
@FeignClient(value = "Pin-Provider")
public interface AdminOrderControllerService {
    @RequestMapping(value = "/admin/order/deliverNameList", method = RequestMethod.GET)
    public JSONObject getExpressInfo();

    @RequestMapping(value = "/admin/order/query", method = RequestMethod.POST)
    public JSONObject getOrderByCondition(HttpServletRequest req, @RequestBody JSONObject queryType);

    @RequestMapping(value = "/admin/order/get-group-order-list", method = RequestMethod.POST)
    public JSONObject getGroupOrderByCondition(HttpServletRequest req, @RequestBody JSONObject queryType);

    @RequestMapping(value = "/admin/order/deliver-product", method = RequestMethod.PUT)
    public JSONObject updateProductStatueToShip(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/admin/order/order-remark", method = RequestMethod.PUT)
    public JSONObject updateMerchantRemark(@RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/admin/discount-setting", method = RequestMethod.POST)
    public JSONObject discountSetting(@RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/admin/refund-order", method = RequestMethod.POST)
    public JSONObject refundOrder(@RequestBody JSONObject requestJSON);
}
