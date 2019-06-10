package cn.edu.neu.shop.pin.controller;

import cn.edu.neu.shop.pin.model.PinOrderGroup;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.service.OrderGroupService;
import cn.edu.neu.shop.pin.service.security.UserService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author LLG
 */
@RestController
@RequestMapping(value = "/order-group")
public class OrderGroupController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderGroupService orderGroupService;

    /**
     * @author flyhero
     * 新建一个团单
     * 将storeId和orderIndividualId传入
     * @param httpServletRequest
     * @param jsonObject
     * @return
     */
    @PostMapping("/create")
    public JSONObject createOrderGroup(HttpServletRequest httpServletRequest, @RequestBody JSONObject jsonObject) {
        PinUser user = userService.whoAmI(httpServletRequest);
        Integer storeId = jsonObject.getInteger("storeId");
        Integer orderIndividualId = jsonObject.getInteger("orderIndividualId");
        // TODO: 没有加收团时间
        int code = orderGroupService.createOrderGroup(user.getId(), storeId, orderIndividualId);
        if(code == OrderGroupService.STATUS_CREATE_ORDER_GROUP_SUCCESS) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS,
                    null);
        } else if(code == OrderGroupService.STATUS_CREATE_ORDER_GROUP_INVALID_ID) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INVALID_DATA, PinConstants.ResponseMessage.INVALID_DATA,
                    null);
        } else if(code == OrderGroupService.STATUS_CREATE_ORDER_GROUP_PERMISSION_DENIED) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INVALID_CREDENTIAL, PinConstants.ResponseMessage.INVALID_CREDENTIAL,
                    null);
        } else if(code == OrderGroupService.STATUS_CREATE_ORDER_GROUP_NOT_ALLOWED) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INVALID_DATA, PinConstants.ResponseMessage.INVALID_DATA,
                     null);
        } else {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, PinConstants.ResponseMessage.INTERNAL_ERROR, null);
        }
    }

    /**
     * @author flyhero
     * 加入团单
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/join")
    public JSONObject joinOrderGroup(HttpServletRequest httpServletRequest) {
        return null;
    }

    /**
     * @author flyhero
     * 根据团单编号获取团单
     * @param orderGroupId
     * @return
     */
    @GetMapping("/by-order-group-id/{orderGroupId}")
    public JSONObject getOrderGroupInfo(@PathVariable(value = "orderGroupId") Integer orderGroupId) {
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

    /**
     * @author flyhero
     * 根据店铺Id，获取某一店铺当前活跃的最近十条拼团记录
     */
    @GetMapping("/by-store-id/{storeId}")
    public JSONObject getTopTenOrderGroups(@PathVariable Integer storeId) {
        List<PinOrderGroup> list = orderGroupService.getTopTenOrderGroups(storeId);
        JSONObject data = new JSONObject();
        data.put("orderGroups", list);
        return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS,
                data);
    }

}
