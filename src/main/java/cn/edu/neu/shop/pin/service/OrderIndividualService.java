package cn.edu.neu.shop.pin.service;

import cn.edu.neu.shop.pin.exception.OrderItemsAreNotInTheSameStoreException;
import cn.edu.neu.shop.pin.exception.ProductSoldOutException;
import cn.edu.neu.shop.pin.exception.RecordNotFoundException;
import cn.edu.neu.shop.pin.mapper.PinOrderIndividualMapper;
import cn.edu.neu.shop.pin.model.PinOrderIndividual;
import cn.edu.neu.shop.pin.model.PinOrderItem;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.model.PinUserAddress;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderIndividualService extends AbstractService<PinOrderIndividual> {

    @Autowired
    private UserRoleListTransferService userRoleListTransferService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private PinOrderIndividualMapper pinOrderIndividualMapper;

    /**
     * 获取最近三个月的OrderIndividual信息
     * @param userId
     * @return
     */
    public List<PinOrderIndividual> getRecentThreeMonthsOrderIndividuals(Integer userId) {
        List<PinOrderIndividual> orderIndividuals =
                pinOrderIndividualMapper.getRecentThreeMonthsOrderIndividuals(userId);
        for(PinOrderIndividual o : orderIndividuals) {
            o.setOrderItems(orderItemService.getOrderItemsByOrderIndividualId(o.getId()));
        }
        return orderIndividuals;
    }

    /** 传入一串PinOrderIndividual，返回它们对应的用户list
     * @param list 一串PinOrderIndividual
     * @return 返回它们对应的用户list
     */
    public List<PinUser> getUsers(List<PinOrderIndividual> list) {
        List<PinUser> users = new ArrayList<>();
        for (PinOrderIndividual item:list){
            users.add(userRoleListTransferService.findById(item.getUserId()));
        }
        return users;
    }

    /**
     * 提交订单，即把一条OrderItem记录变为Submitted
     * @param user
     * @param list
     * @param addressId
     * @return
     * @throws OrderItemsAreNotInTheSameStoreException
     * @throws ProductSoldOutException
     */
    public PinOrderIndividual addOrderIndividual(PinUser user, List<PinOrderItem> list, Integer addressId) throws OrderItemsAreNotInTheSameStoreException, ProductSoldOutException {
        PinUserAddress address = addressService.findById(addressId);
        if(address == null) {
            throw new RecordNotFoundException("地址ID不正确");
        }
        boolean isSameStore = productService.isBelongSameStore(list);
        //如果属于一家店铺
        if (isSameStore) {
            Integer amount = orderItemService.getProductAmount(list);
            if (amount == -1) {
                //库存不够，只能终止这次创建orderIndividual
                throw new ProductSoldOutException("库存不足");
            }
            int storeId = productService.getProductById(list.get(0).getProductId()).getStoreId();    // 店铺id
            BigDecimal originallyPrice = orderItemService.getProductTotalPrice(list);   // 计算本来的价格
            BigDecimal shippingFee = orderItemService.getAllShippingFee(list);  // 邮费
            BigDecimal totalPrice = originallyPrice.add(shippingFee);   //总费用
//            OrderItemService.PayDetail payDetail = orderItemService.new PayDetail(user.getId(), totalPrice);    //支付详情
            BigDecimal totalCost = orderItemService.getTotalCost(list);
            String addressString = address.getProvince()+address.getCity()+address.getDistrict()+address.getDetail();
            PinOrderIndividual orderIndividual = new PinOrderIndividual(null, storeId, user.getId(),
                    address.getRealName(), address.getPhone(), addressString,
                    orderItemService.getProductAmount(list), totalPrice/*总价格 邮费加本来的费用*/,
                    shippingFee,null, /*卖家可以改动实际支付的邮费，修改的时候总价格也要修改，余额支付，实际支付也要改*/
                    null, null,false, null,
                    new Date(System.currentTimeMillis()),0,0,null,null,null,
                    null,null,null,null,null,null,null,null,null,totalCost);
            this.save(orderIndividual);
            //将list中的PinOrderItem挂载到PinOrderIndividual上
            orderItemService.amountOrderItems(list,orderIndividual.getId());
            return orderIndividual;
        }
        //如果不属于一家店铺
        else {
            throw new OrderItemsAreNotInTheSameStoreException("不属于一家店铺");
        }
    }

}
