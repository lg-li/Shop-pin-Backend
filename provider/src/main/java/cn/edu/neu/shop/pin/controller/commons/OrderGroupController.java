package cn.edu.neu.shop.pin.controller.commons;

import cn.edu.neu.shop.pin.model.PinOrderGroup;
import cn.edu.neu.shop.pin.model.PinOrderIndividual;
import cn.edu.neu.shop.pin.model.PinStore;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.service.OrderGroupService;
import cn.edu.neu.shop.pin.service.OrderIndividualService;
import cn.edu.neu.shop.pin.service.StoreService;
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
@RequestMapping(value = "/commons/order-group")
public class OrderGroupController {

    private final UserService userService;

    private final OrderIndividualService orderIndividualService;

    private final OrderGroupService orderGroupService;

    private final StoreService storeService;

    @Autowired
    public OrderGroupController(UserService userService, OrderIndividualService orderIndividualService, OrderGroupService orderGroupService, StoreService storeService) {
        this.userService = userService;
        this.orderIndividualService = orderIndividualService;
        this.orderGroupService = orderGroupService;
        this.storeService = storeService;
    }

    /**
     * @param httpServletRequest HttpServlet请求体
     * @param jsonObject         包含storeId和orderIndividualId
     * @return 响应JSON
     * @author flyhero
     * 新建一个团单
     * 将storeId和orderIndividualId传入
     */
    @PostMapping("/create")
    public JSONObject createOrderGroup(HttpServletRequest httpServletRequest, @RequestBody JSONObject jsonObject) {
        PinUser user = userService.whoAmI(httpServletRequest);
        Integer storeId = jsonObject.getInteger("storeId");
        Integer orderIndividualId = jsonObject.getInteger("orderIndividualId");
        int code = orderGroupService.createOrderGroup(user.getId(), storeId, orderIndividualId);
        if (code == OrderGroupService.STATUS_SUCCESS) {
            // 创建成功
            PinOrderIndividual orderIndividual = orderIndividualService.findById(orderIndividualId);
            JSONObject data = new JSONObject();
            data.put("orderGroupId", orderIndividual.getOrderGroupId());
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, "创团成功！",
                    data);
        } else if (code == OrderGroupService.STATUS_INVALID_ID) {
            // 创建失败，店铺ID不符
            return ResponseWrapper.wrap(PinConstants.StatusCode.INVALID_DATA, "创团失败，店铺ID有误！",
                    null);
        } else if (code == OrderGroupService.STATUS_PERMISSION_DENIED) {
            // 创建失败，用户ID不符
            return ResponseWrapper.wrap(PinConstants.StatusCode.INVALID_CREDENTIAL, "创团失败，用户ID有误！",
                    null);
        } else if (code == OrderGroupService.STATUS_NOT_ALLOWED) {
            // 创建失败，订单状态不符合创团条件（未付款或已发货）
            return ResponseWrapper.wrap(PinConstants.StatusCode.INVALID_DATA, "创团失败，订单不符合条件！",
                    null);
        } else if (code > 0) {
            // 创建失败，已经在其他团单中，返回已有团单id
            JSONObject data = new JSONObject();
            PinOrderIndividual orderIndividual = orderIndividualService.findById(orderIndividualId);
            Integer orderGroupId = orderIndividual.getOrderGroupId();
            data.put("orderGroupId", orderGroupId);
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, "创团失败，已经在某一团单中！", data);
        }
        return null;
    }

    /**
     * @param httpServletRequest HttpServlet请求体
     * @return 响应JSON
     * @author flyhero
     * 加入团单
     */
    @PostMapping("/join")
    public JSONObject joinOrderGroup(HttpServletRequest httpServletRequest, @RequestBody JSONObject jsonObject) {
        PinUser user = userService.whoAmI(httpServletRequest);
        Integer storeId = jsonObject.getInteger("storeId");
        Integer orderIndividualId = jsonObject.getInteger("orderIndividualId");
        Integer orderGroupId = jsonObject.getInteger("orderGroupId");
        int code = orderGroupService.joinOrderGroup(user.getId(), storeId, orderIndividualId, orderGroupId);
        if (code == OrderGroupService.STATUS_SUCCESS) {
            // 团单加入成功
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, "团单加入成功！",
                    null);
        } else if (code == OrderGroupService.STATUS_INVALID_ID) {
            // 店铺ID不符，返回ID错误
            return ResponseWrapper.wrap(PinConstants.StatusCode.INVALID_DATA, "加团失败，店铺ID有误！",
                    null);
        } else if (code == OrderGroupService.STATUS_PERMISSION_DENIED) {
            // 创建失败，用户ID不符
            return ResponseWrapper.wrap(PinConstants.StatusCode.INVALID_CREDENTIAL, "加团失败，用户ID有误！",
                    null);
        } else if (code == OrderGroupService.STATUS_NOT_ALLOWED) {
            // 给定的团单不满足情况：1.isPaid不是未付款 2.status不是0-待发货
            return ResponseWrapper.wrap(PinConstants.StatusCode.INVALID_DATA, "无法加入此团，因您在本店仍有一个未结束的团。",
                    null);
        } else if (code == OrderGroupService.STATUS_JOIN_ORDER_GROUP_IS_ENDED) {
            // 加团失败，给定的团单已结束
            return ResponseWrapper.wrap(PinConstants.StatusCode.INVALID_DATA, "此团单已结束，无法加入。",
                    null);
        } else if (code == OrderGroupService.STATUS_JOIN_ORDER_GROUP_IS_FULL) {
            // 加团失败，给定的团单人数已满
            return ResponseWrapper.wrap(PinConstants.StatusCode.INVALID_DATA, "很抱歉，此团单人数已满。",
                    null);
        } else if (code > 0) {
            // 加团失败，已经在其他团单中，返回已有团单id
            JSONObject data = new JSONObject();
            PinOrderIndividual orderIndividual = orderIndividualService.findById(orderIndividualId);
            data.put("orderGroupId", orderIndividual.getOrderGroupId());
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, "加团失败，已经在某一团单中！", data);
        }
        return null;
    }

    /**
     * @param httpServletRequest HttpServlet请求体
     * @param jsonObject:        包括storeId, orderIndividualId, orderGroupId
     * @return 响应JSON
     * @author flyhero
     * 退出团单
     */
    @PostMapping("/quit")
    public JSONObject quitOrderGroup(HttpServletRequest httpServletRequest, @RequestBody JSONObject jsonObject) {
        PinUser user = userService.whoAmI(httpServletRequest);
        Integer orderIndividualId = jsonObject.getInteger("orderIndividualId");
        Integer orderGroupId = jsonObject.getInteger("orderGroupId");
        int code = orderGroupService.quitOrderGroup(user.getId(), orderIndividualId, orderGroupId);
        if (code == OrderGroupService.STATUS_SUCCESS) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, "团单退出成功！",
                    null);
        } else if (code == OrderGroupService.STATUS_QUIT_ORDER_GROUP_FAILED) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, "退出失败！团单创建者无法退出团单！",
                    null);
        }
        return null;
    }

    /**
     * @param orderGroupId 团单ID
     * @return 响应JSON
     * @author flyhero
     * 根据团单编号获取团单
     */
    @GetMapping("/by-order-group-id/{orderGroupId}")
    public JSONObject getOrderGroupByOrderGroupId(@PathVariable(value = "orderGroupId") Integer orderGroupId) {
        try {
            PinOrderGroup orderGroup = orderGroupService.findById(orderGroupId);
            List<PinUser> list = orderGroupService.getUsersByOrderGroup(orderGroup.getId());
            JSONObject data = new JSONObject();
            data.put("orderGroup", orderGroup);
            data.put("users", list);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
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
    public JSONObject getTopTenOrderGroupsByStoreId(@PathVariable Integer storeId) {
        List<PinOrderGroup> list = orderGroupService.getTopTenOrderGroups(storeId);
        System.out.println("list.size(): " + list.size());
        PinStore store = storeService.getStoreById(storeId);
        for (PinOrderGroup orderGroup : list) {
            orderGroup.setStore(store);
            List<PinOrderIndividual> orderIndividuals = orderIndividualService.getOrderIndividualsByOrderGroupId(orderGroup.getId());
            orderGroup.setOrderIndividuals(orderIndividuals);
        }
        JSONObject data = new JSONObject();
        data.put("orderGroups", list);
        return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS,
                data);
    }

}
