package cn.edu.neu.shop.pin.consumer.service.commons;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Service
public interface PaymentControllerService {
    @RequestMapping(value = "/commons/payment/unified-pay", method = RequestMethod.POST)
    public JSONObject unifiedPay(@RequestBody JSONObject requestObject);
}
