package cn.edu.neu.shop.pin.customer.controller;

import cn.edu.neu.shop.pin.customer.service.*;
import cn.edu.neu.shop.pin.customer.service.security.UserService;
import cn.edu.neu.shop.pin.model.PinOrderGroup;
import cn.edu.neu.shop.pin.model.PinOrderIndividual;
import cn.edu.neu.shop.pin.model.PinOrderItem;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONObject;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
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
     * TODO: 未测试
     * written by flyhero
     * 将商品添加到购物车中（新建一条OrderItem记录）
     * @param httpServletRequest
     * @param requestJSON
     * @return
     */
    @PostMapping("/add-order-item")
    public JSONObject addOrderItem(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        try {
            PinUser user = userService.whoAmI(httpServletRequest);
            Integer productId = requestJSON.getInteger("productId");
            Integer skuId = requestJSON.getInteger("skuId");
            Integer amount = requestJSON.getInteger("amount");
            int code = orderItemService.addOrderItem(user.getId(), productId, skuId, amount);
            if(code == OrderItemService.STATUS_ADD_ORDER_ITEM_SUCCESS) {
                return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, null);
            }  else if(code == OrderItemService.STATUS_ADD_ORDER_ITEM_INVALID_ID) {
                return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, "添加购物车失败", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据团单编号获取团单
     * @param orderGroupId
     * @return
     */
    @GetMapping("/get-group-order")
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

    /**
     * TODO:ydy 未测试
     * 创建一个pinOrderIndividual
     */
    @PostMapping("/create-order-individual")
    public JSONObject createOrderIndividual(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestObject) {
        try {
            PinUser user = userService.whoAmI(httpServletRequest);
            List<PinOrderItem> list = orderItemService.getItemListByJSONArray(requestObject.getJSONArray("items"));
            boolean isSameStore = productService.isBelongSameStore(list);
            //如果属于一家店铺
            if (isSameStore) {
                int storeId = productService.getProductById(list.get(0).getProductId()).getStoreId();    // 店铺id
                BigDecimal originallyPrice = orderItemService.getProductTotalPrice(list);   // 计算本来的价格
                BigDecimal shippingFee = orderItemService.getAllShippingFee(list);  // 邮费
                BigDecimal totalPrice = originallyPrice.add(shippingFee);   //总费用
                OrderItemService.PayDetail payDetail = orderItemService.new PayDetail(user.getId(), totalPrice);    //支付详情
                BigDecimal totalCost = orderItemService.getTotalCost(list);

                PinOrderIndividual orderIndividual = new PinOrderIndividual(null, storeId, user.getId(),
                        user.getNickname(), user.getPhone(), requestObject.getString("address"),
                        orderItemService.getProductAmount(list), totalPrice/*总价格 邮费加本来的费用*/,
                        shippingFee,payDetail.getPayPrice(),shippingFee/*卖家可以改动实际支付的邮费，修改的时候总价格也要修改，余额支付，实际支付也要改*/,
                        payDetail.getBalancePaidPrice(), null,false,payDetail.getPayType(),
                        new Date(System.currentTimeMillis()),0,0,null,null,null,
                        null,null,null,null,null,null,null,null,null,totalCost);
                orderIndividualService.save(orderIndividual);
                //将list中的PinOrderItem挂载到PinOrderIndividual上
                orderItemService.amountOrderItems(list,orderIndividual.getId());
                return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, orderIndividual);
            }
            //如果不属于一家店铺
            else {
                //TODO: ydy 返回前端想要的样子
                return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, "不属于一家店铺");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    @GetMapping("/get-order-items")
    public JSONObject getAllOrderItems(HttpServletRequest httpServletRequest) {
        try {
            PinUser user = userService.whoAmI(httpServletRequest);
            Integer userId = user.getId();
            List<PinOrderItem> list = orderItemService.getAllOrderItems(userId);
            JSONObject data = new JSONObject();
            data.put("items", list);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS,
                    data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }
}
