package cn.edu.neu.shop.pin.controller;

import cn.edu.neu.shop.pin.model.PinOrderGroup;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.service.OrderGroupService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author LLG
 */
@RestController
@RequestMapping(value = "order-group")
public class OrderGroupController {

    @Autowired
    private OrderGroupService orderGroupService;

    /**
     * 根据团单编号获取团单
     * @param orderGroupId
     * @return
     */
    @GetMapping("/order-group/{orderGroupId}")
    public JSONObject getGroupOrderInfo(@PathVariable(value = "orderGroupId") Integer orderGroupId) {
        try {
            PinOrderGroup orderGroupInfo = orderGroupService.findById(orderGroupId);
            List<PinUser> list = orderGroupService.getUsersByOrderGroup(orderGroupInfo);
            JSONObject data = new JSONObject();
            data.put("orderGroup", orderGroupInfo);
            data.put("users", list);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS,
                    data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

}
