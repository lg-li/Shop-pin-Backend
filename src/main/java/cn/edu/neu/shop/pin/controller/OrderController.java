package cn.edu.neu.shop.pin.controller;

import cn.edu.neu.shop.pin.exception.OrderItemsAreNotInTheSameStoreException;
import cn.edu.neu.shop.pin.exception.ProductSoldOutException;
import cn.edu.neu.shop.pin.model.PinOrderGroup;
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
public class OrderController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderIndividualService orderIndividualService;

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
            PinOrderGroup orderGroupInfo = orderService.getOrderGroupInfo(orderGroupId);
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
     * @param httpServletRequest 请求对象
     * @param requestObject      请求体JSON对象
     * @return 返回JSON
     * @author YDY LLG
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
     * @author flyhero
     * 获取所有订单信息
     * @param httpServletRequest
     * @return
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
     * @param httpServletRequest
     * @param requestJSON
     * @return
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
            if(code == OrderItemService.STATUS_ADD_ORDER_ITEM_SUCCESS) {
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
     * @param jsonObject         请求体JSON
     * @return
     * @author flyhero
     * 删除订单信息
     */
    @DeleteMapping("/order-items")
    public JSONObject deleteOrderItems(HttpServletRequest httpServletRequest, @RequestBody JSONObject jsonObject) {
        try {
            JSONArray array = jsonObject.getJSONArray("orderItems");
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
     * @param httpServletRequest HTTP请求对象
     * @return
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
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }
}
