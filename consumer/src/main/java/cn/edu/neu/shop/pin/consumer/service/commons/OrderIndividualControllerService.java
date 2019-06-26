package cn.edu.neu.shop.pin.consumer.service.commons;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Service
public interface OrderIndividualControllerService {
    @RequestMapping(value = "/commons/order/order-individual", method = RequestMethod.POST)
    public JSONObject createOrderIndividual(@RequestBody JSONObject requestObject);

    @RequestMapping(value = "/commons/order/order-individual/recent", method = RequestMethod.GET)
    public JSONObject getRecentThreeMonthsOrderIndividuals();

    @RequestMapping(value = "/commons/order/order-individual/confirm-receipt", method = RequestMethod.POST)
    public JSONObject confirmReceipt(@RequestBody JSONObject requestJSON);

    @RequestMapping(value = "/commons/order/refund", method = RequestMethod.POST)
    public JSONObject refundOrder(@RequestBody JSONObject requestJSON);
}
