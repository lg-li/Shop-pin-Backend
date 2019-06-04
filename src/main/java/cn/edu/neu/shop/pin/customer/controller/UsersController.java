package cn.edu.neu.shop.pin.customer.controller;

import cn.edu.neu.shop.pin.customer.service.*;
import cn.edu.neu.shop.pin.customer.service.security.UserService;
import cn.edu.neu.shop.pin.model.PinOrderIndividual;
import cn.edu.neu.shop.pin.model.PinOrderItem;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.model.PinUserAddress;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONObject;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;

@RestController
@RequestMapping("/commons/user")
public class UsersController {

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private UserProductRecordService userProductRecordService;

    @Autowired
    private UserStoreCollectionService userStoreCollectionService;

    @Autowired
    private UserProductCollectionService userProductCollectionService;

    @PostMapping("/user-info")
    public JSONObject getUserInfo(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        try {
            PinUser user = userService.whoAmI(httpServletRequest);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS,
                    userService.findById(user.getId()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    /**
     * 根据用户ID，查询该用户的所有收获地址
     */
    @GetMapping("/address")
    public JSONObject getAddressByUserId(HttpServletRequest httpServletRequest) {
        try {
            PinUser user = userService.whoAmI(httpServletRequest);
            JSONObject data = new JSONObject();
            data.put("list", addressService.getAllAddressesByUserId(user.getId()));
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    @PostMapping("/address")
    public JSONObject createAddress(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        try {
            PinUser user = userService.whoAmI(httpServletRequest);
            String realName = requestJSON.getString("realName");
            String phone = requestJSON.getString("phone");
            String province = requestJSON.getString("province");
            String city = requestJSON.getString("city");
            String district = requestJSON.getString("district");
            String detail = requestJSON.getString("detail");
            Integer postCode = requestJSON.getInteger("postCode");
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS,
                    addressService.createAddressByUserId(user.getId(), realName, phone, province, city, district, detail, postCode));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    @DeleteMapping("/address")
    public JSONObject deleteAddress(HttpServletRequest httpServletRequest, @PathVariable(value = "addressId") int addressId) {
        try {
            PinUser user = userService.whoAmI(httpServletRequest);
            int code = addressService.deleteAddressByUserId(addressId, user.getId());
            if (code == AddressService.STATUS_DELETE_ADDRESS_SUCCESS) {
                return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, null);
            } else if (code == AddressService.STATUS_DELETE_ADDRESS_INVALID_ID) {
                return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, "删除失败", null);
            } else if (code == AddressService.STATUS_DELETE_ADDRESS_PERMISSION_DENIED) {
                return ResponseWrapper.wrap(PinConstants.StatusCode.PERMISSION_DENIED, "无权限删除", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
        return null;
    }

    @PutMapping("/address")
    public JSONObject updateAddress(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        try {
            PinUser user = userService.whoAmI(httpServletRequest);
            PinUserAddress addressToUpdate = JSONObject.toJavaObject(requestJSON, PinUserAddress.class);
            if (addressService.updateAddressByUserId(user.getId(), addressToUpdate) == null) {
                // 无权
                return ResponseWrapper.wrap(PinConstants.StatusCode.PERMISSION_DENIED, "无权限删除", null);
            }
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    /**TODO:ydy 未测试
     * 创建一个pinOrderIndividual
     */
    @PostMapping("/order")
    public JSONObject createOrderIndividual(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestObject) {
        try {
            PinUser user = userService.whoAmI(httpServletRequest);
            ArrayList<PinOrderItem> list = orderItemService.getItemListByJSONArray(requestObject.getJSONArray("items"));
            boolean isSameStore = productService.isBelongSameStore(list);
            //如果属于一家店铺
            if (isSameStore) {
                int storeId = productService.getProductByProductId(list.get(0).getProductId()).getStoreId();    // 店铺id
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

                //TODO:将新建的orderIndividual写到数据库中
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

    @GetMapping("/product-visit-record")
    public JSONObject getUserProductRecord(HttpServletRequest httpServletRequest) {
        try{
            PinUser user = userService.whoAmI(httpServletRequest);
            JSONObject data = new JSONObject();
            data.put("list", userProductRecordService.getUserProductVisitRecord(user.getId()));
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    /**
     * 获取商品收藏product-collection
     * @return
     */
    @GetMapping("/user-product-collection")
    public JSONObject getUserProductCollection(HttpServletRequest httpServletRequest) {
        PinUser user = userService.whoAmI(httpServletRequest);
        try {
            JSONObject data = new JSONObject();
            data.put("list", userProductCollectionService.getUserProductCollection(user.getId()));
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    /**
     * 获取店铺收藏store-collection
     * @param userId
     * @return
     */
    @GetMapping("/user-store-collection/{userId}")
    public JSONObject getUserStoreCollection(@PathVariable(value = "userId") Integer userId) {
        try {
            JSONObject data = new JSONObject();
            data.put("list", userStoreCollectionService.getUserStoreCollection(userId));
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    @PostMapping("/product-collection")
    public JSONObject addProductToCollection(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        try{
            PinUser user = userService.whoAmI(httpServletRequest);
            Integer userId = user.getId();
            Integer productId = requestJSON.getInteger("productId");
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS,
                    userProductCollectionService.addProductToCollection(productId, userId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }
}
