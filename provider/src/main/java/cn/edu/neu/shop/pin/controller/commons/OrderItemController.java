package cn.edu.neu.shop.pin.controller.commons;

import cn.edu.neu.shop.pin.exception.InvalidOperationException;
import cn.edu.neu.shop.pin.exception.PermissionDeniedException;
import cn.edu.neu.shop.pin.exception.RecordNotFoundException;
import cn.edu.neu.shop.pin.model.PinOrderItem;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.service.OrderItemService;
import cn.edu.neu.shop.pin.service.security.UserService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author flyhero LLG
 * 购物车个商品选品相关操作 controller
 */
@RestController
@RequestMapping("/commons/order")
public class OrderItemController {

    private final UserService userService;

    private final OrderItemService orderItemService;

    @Autowired
    public OrderItemController(UserService userService, OrderItemService orderItemService) {
        this.userService = userService;
        this.orderItemService = orderItemService;
    }

    /**
     * @param httpServletRequest HttpServlet请求体
     * @return 获取OrderItem的成功标志
     * @author flyhero
     * 获取购物车中所有OrderItem的信息
     */
    @GetMapping("/order-items")
    public JSONObject getAllOrderItems(HttpServletRequest httpServletRequest) {
        try {
            PinUser user = userService.whoAmI(httpServletRequest);
            Integer userId = user.getId();
            List<PinOrderItem> list = orderItemService.getAllOrderItems(userId);
            JSONObject data = new JSONObject();
            data.put("orderItems", list);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS,
                    data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }


    /**
     * @param httpServletRequest HttpServlet请求体
     * @param requestJSON        请求体JSON
     * @return 获取OrderItem的成功标志
     * @author flyhero
     * 将商品添加到购物车中（新建一条OrderItem记录）
     */
    @PostMapping("/order-item")
    public JSONObject addOrderItem(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        try {
            PinUser user = userService.whoAmI(httpServletRequest);
            Integer productId = requestJSON.getInteger("productId");
            Integer skuId = requestJSON.getInteger("skuId");
            Integer amount = requestJSON.getInteger("amount");
            int code = orderItemService.createOrderItem(user.getId(), productId, skuId, amount);
            if (code == OrderItemService.STATUS_ADD_ORDER_ITEM_SUCCESS) {
                return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, null);
            } else if (code == OrderItemService.STATUS_ADD_ORDER_ITEM_INVALID_ID) {
                return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, "添加购物车失败", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, "添加购物车失败", null);
        }
        return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, "添加购物车失败", null);
    }

    /**
     * @param httpServletRequest HTTP请求对象
     * @param requestJSON        请求体JSON
     * @return 删除OrderItem成功与否
     * @author flyhero
     * 删除订单信息
     */
    @DeleteMapping("/order-items")
    public JSONObject deleteOrderItems(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        try {
            JSONArray array = requestJSON.getJSONArray("orderItems");
            List<Integer> list = array.toJavaList(Integer.class);
            PinUser user = userService.whoAmI(httpServletRequest);
            int code = orderItemService.deleteOrderItems(user.getId(), list);
            if (code == OrderItemService.STATUS_DELETE_ORDER_ITEM_SUCCESS) {
                return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, null);
            } else if (code == OrderItemService.STATUS_DELETE_ORDER_ITEM_INVALID_ID) {
                return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, "删除失败", null);
            } else if (code == OrderItemService.STATUS_DELETE_ORDER_ITEM_PERMISSION_DENIED) {
                return ResponseWrapper.wrap(PinConstants.StatusCode.PERMISSION_DENIED, "无权限删除", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
        return null;
    }

    /**
     * @param httpServletRequest HttpServlet请求体
     * @param requestJSON        包含：orderItemId, amount
     * @return 响应JSON
     * @author flyhero
     * 修改购物车中某商品的数量
     */
    @PostMapping("/order-item/change-amount")
    public JSONObject changeOrderItemAmount(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        PinUser user = userService.whoAmI(httpServletRequest);
        Integer orderItemId = requestJSON.getInteger("orderItemId");
        Integer amount = requestJSON.getInteger("amount");
        try {
            orderItemService.changeOrderItemAmount(user.getId(), orderItemId, amount);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, "商品数量修改成功！", null);
        } catch (PermissionDeniedException e) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INVALID_CREDENTIAL, e.getMessage(), null);
        } catch (RecordNotFoundException | InvalidOperationException e) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INVALID_DATA, e.getMessage(), null);
        }
    }
}
