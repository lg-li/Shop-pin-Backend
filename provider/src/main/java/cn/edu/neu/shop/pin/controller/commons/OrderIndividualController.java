package cn.edu.neu.shop.pin.controller.commons;

import cn.edu.neu.shop.pin.exception.*;
import cn.edu.neu.shop.pin.model.PinOrderIndividual;
import cn.edu.neu.shop.pin.model.PinOrderItem;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.service.*;
import cn.edu.neu.shop.pin.service.security.UserService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/commons/order")
public class OrderIndividualController {

    private final UserService userService;

    private final OrderItemService orderItemService;

    private final OrderIndividualService orderIndividualService;

    @Autowired
    public OrderIndividualController(UserService userService, OrderItemService orderItemService, OrderIndividualService orderIndividualService) {
        this.userService = userService;
        this.orderItemService = orderItemService;
        this.orderIndividualService = orderIndividualService;
    }

    /**
     * @param httpServletRequest 请求对象
     * @param requestObject      请求体JSON对象
     * @return 返回JSON
     * @author flyhero
     * 创建一个pinOrderIndividual
     */
    @PostMapping("/order-individual")
    public JSONObject createOrderIndividual(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestObject) {
        try {
            PinUser user = userService.whoAmI(httpServletRequest);
            List<PinOrderItem> list = orderItemService.getItemListByJSONArray(requestObject.getJSONArray("orderItemIds"));
            Integer addressId = requestObject.getInteger("addressId");
            String userRemark = requestObject.getString("userRemark");
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, orderIndividualService.addOrderIndividual(user, list, addressId, userRemark));
        } catch (ProductSoldOutException e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.PRODUCT_SOLD_OUT, e.getMessage(), null);
        } catch (OrderItemsAreNotInTheSameStoreException e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INVALID_DATA, e.getMessage(), null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    /**
     * @param httpServletRequest HTTP请求对象
     * @return 请求体JSON
     * @author flyhero
     * 获取近三个月所有的orderIndividuals
     */
    @GetMapping("/order-individual/recent")
    public JSONObject getRecentThreeMonthsOrderIndividuals(HttpServletRequest httpServletRequest) {
        PinUser user = userService.whoAmI(httpServletRequest);
        try {
            List<PinOrderIndividual> list = orderIndividualService.getRecentThreeMonthsOrderIndividuals(user.getId());
            JSONObject data = new JSONObject();
            data.put("orderIndividuals", list);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
        } catch (Exception e) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    /**
     * @param httpServletRequest HttpServlet请求体
     * @param requestJSON        包含orderIndividualId——以此来确认该订单已收货
     * @return 收货成功与否结果 JSON
     * @author flyhero
     * 确认收货
     */
    @PostMapping("/order-individual/confirm-receipt")
    public JSONObject confirmReceipt(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        PinUser user = userService.whoAmI(httpServletRequest);
        Integer orderIndividualId = requestJSON.getInteger("orderIndividualId");
        int code = orderIndividualService.confirmReceipt(user.getId(), orderIndividualId);
        if (code == OrderIndividualService.STATUS_CONFIRM_SUCCESS) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, "确认收货成功！", null);
        } else if (code == OrderIndividualService.STATUS_CONFIRM_FAILED) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INVALID_DATA, "无法确认收货！请检查订单状态！", null);
        } else if (code == OrderIndividualService.STATUS_CONFIRM_PERMISSION_DENIED) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INVALID_CREDENTIAL, "用户权限不够！", null);
        } else {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, PinConstants.ResponseMessage.INTERNAL_ERROR, null);
        }
    }

    @PostMapping("/refund")
    public JSONObject refundOrder(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        try {
            PinOrderIndividual orderIndividual = new PinOrderIndividual();
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, null);
        } catch (Exception e) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

}
