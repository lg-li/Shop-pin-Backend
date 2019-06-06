package cn.edu.neu.shop.pin.service;

import cn.edu.neu.shop.pin.exception.OrderItemsAreNotInTheSameStoreException;
import cn.edu.neu.shop.pin.exception.ProductSoldOutException;
import cn.edu.neu.shop.pin.model.PinOrderIndividual;
import cn.edu.neu.shop.pin.model.PinOrderItem;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderIndividualService extends AbstractService<PinOrderIndividual> {

    @Autowired
    UserRoleListTransferService userRoleListTransferService;

    @Autowired
    ProductService productService;

    @Autowired
    OrderItemService orderItemService;

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
     * @param address
     * @return
     * @throws OrderItemsAreNotInTheSameStoreException
     * @throws ProductSoldOutException
     */
    public PinOrderIndividual addOrderIndividual(PinUser user, List<PinOrderItem> list, String address) throws OrderItemsAreNotInTheSameStoreException, ProductSoldOutException {
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
            OrderItemService.PayDetail payDetail = orderItemService.new PayDetail(user.getId(), totalPrice);    //支付详情
            BigDecimal totalCost = orderItemService.getTotalCost(list);

            PinOrderIndividual orderIndividual = new PinOrderIndividual(null, storeId, user.getId(),
                    user.getNickname(), user.getPhone(), address,
                    orderItemService.getProductAmount(list), totalPrice/*总价格 邮费加本来的费用*/,
                    shippingFee,payDetail.getPayPrice(),shippingFee/*卖家可以改动实际支付的邮费，修改的时候总价格也要修改，余额支付，实际支付也要改*/,
                    payDetail.getBalancePaidPrice(), null,false,payDetail.getPayType(),
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
