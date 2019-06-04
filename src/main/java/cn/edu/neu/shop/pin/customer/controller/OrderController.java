package cn.edu.neu.shop.pin.customer.controller;

import cn.edu.neu.shop.pin.customer.service.OrderGroupService;
import cn.edu.neu.shop.pin.customer.service.OrderItemService;
import cn.edu.neu.shop.pin.customer.service.OrderService;
import cn.edu.neu.shop.pin.model.PinOrderGroup;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONObject;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/commons/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderGroupService orderGroupService;

    @GetMapping("/beg-group-order")
    public JSONObject getGroupOrderInfo(@RequestParam Integer orderGroupId) {
        try{
            PinOrderGroup orderGroupInfo =  orderService.getOrderGroupInfo(orderGroupId);
            List<PinUser> list = orderGroupService.getUsersByOrderGroup(orderGroupInfo);
            JSONObject data = new JSONObject();
            data.put("orderGroup",orderGroupInfo);
            data.put("users",list);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS,
                    data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

}
