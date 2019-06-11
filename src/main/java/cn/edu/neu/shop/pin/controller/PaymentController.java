package cn.edu.neu.shop.pin.controller;


import cn.edu.neu.shop.pin.exception.InsufficientBalanceException;
import cn.edu.neu.shop.pin.exception.PermissionDeniedException;
import cn.edu.neu.shop.pin.exception.RecordNotFoundException;
import cn.edu.neu.shop.pin.exception.RepeatPaymentException;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.service.finance.PaymentService;
import cn.edu.neu.shop.pin.service.security.UserService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/commons/payment")
public class PaymentController {

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentService paymentService;

    @PostMapping(value = "/unified-pay")
    public JSONObject unifiedPay(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestObject) {
        try {
            PinUser user = userService.whoAmI(httpServletRequest);
            Integer orderIndividualId = requestObject.getInteger("orderIndividualId");
            String paymentType = requestObject.getString("paymentType");
            switch (paymentType) {
                case "BALANCE":
                    paymentService.payIndividualOrderByBalance(orderIndividualId, user.getId());
                    return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, "支付成功", null);
                case "WECHAT":
                    // TODO: 微信支付
                    return ResponseWrapper.wrap(PinConstants.StatusCode.INVALID_DATA, "暂不支持的支付类型", null);
                default:
                    return ResponseWrapper.wrap(PinConstants.StatusCode.INVALID_DATA, "非法的支付类型", null);
            }
        } catch (PermissionDeniedException | RecordNotFoundException e) {
            // 非法访问限制
            return ResponseWrapper.wrap(PinConstants.StatusCode.INVALID_DATA, e.getMessage(), null);
        } catch (InsufficientBalanceException e) {
            // 余额不足
            return ResponseWrapper.wrap(PinConstants.StatusCode.PAY_INSUFFICIENT_BALANCE, e.getMessage(), null);
        } catch (RepeatPaymentException e) {
            // 已经支付
            return ResponseWrapper.wrap(PinConstants.StatusCode.PAY_REPEAT_PAYMENT, e.getMessage(), null);
        }
    }
}
