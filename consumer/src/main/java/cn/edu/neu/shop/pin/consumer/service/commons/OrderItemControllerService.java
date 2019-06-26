package cn.edu.neu.shop.pin.consumer.service.commons;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Service
public interface OrderItemControllerService {
    @RequestMapping(value = "/commons/order/order-items", method = RequestMethod.GET)
    public JSONObject getAllOrderItems();

    @RequestMapping(value = "/commons/order/order-item", method = RequestMethod.POST)
    public JSONObject addOrderItem(@RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/commons/order/order-items", method = RequestMethod.DELETE)
    public JSONObject deleteOrderItems(@RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/commons/order/order-item/change-amount", method = RequestMethod.POST)
    public JSONObject changeOrderItemAmount(@RequestBody JSONObject requestJSON);
}
