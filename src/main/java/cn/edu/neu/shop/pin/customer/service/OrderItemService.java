package cn.edu.neu.shop.pin.customer.service;

import cn.edu.neu.shop.pin.mapper.PinOrderItemMapper;
import cn.edu.neu.shop.pin.mapper.PinUserMapper;
import cn.edu.neu.shop.pin.model.PinOrderItem;
import cn.edu.neu.shop.pin.model.PinProduct;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.util.PinConstants;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;

@Service
public class OrderItemService {

    @Autowired
    private PinOrderItemMapper pinOrderItemMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRoleListTransferService userService;


    /**
     * TODO:未测试
     * 通过传进来的JSONArray 产生 PinOrderItem 的array
     *
     * @param array 传入的JSONArray 里面是order_item的id
     * @return 返回由PinOrderItem组成的ArrayList
     */
    public ArrayList<PinOrderItem> getItemListByJSONArray(JSONArray array) {
        ArrayList<PinOrderItem> list = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            //这个selectByPrimaryKey 不知道有没有用
            list.add(pinOrderItemMapper.selectByPrimaryKey(array.getInteger(i)));
        }
        return list;
    }


    /**
     * TODO:未测试
     * 通过JSONArray 传入PinOrderItem的数组
     *
     * @param array 数组 里面都是PinOrderItem的对象
     * @return 返回商品总数
     */
    public Integer getProductAmount(ArrayList<PinOrderItem> array) {
        Integer amount = 0;
        for (PinOrderItem item : array) {
            amount += item.getAmount();
        }
        return amount;
    }

    /**
     * TODO:未测试
     * 通过JSONArray 传入PinOrderItem的数组
     *
     * @param array 数组 里面都是PinOrderItem的对象
     * @return 返回商品总数
     */
    public BigDecimal getProductTotalPrice(ArrayList<PinOrderItem> array) {
        //BigDecimal 尽量用字符串初始化
        BigDecimal price = new BigDecimal("0");
        for (PinOrderItem item : array) {
            price = price.add(item.getTotalPrice());
        }
        return price;
    }

    /**
     * TODO:未测试
     *
     * @param array 传入一个PinOrderItem数组
     * @return 返回总的邮费
     */
    public BigDecimal getAllShippingFee(ArrayList<PinOrderItem> array) {
        BigDecimal shippingFee = new BigDecimal("0");
        PinProduct product;
        for (PinOrderItem item : array) {
            product = productService.getProductByProductId(item.getProductId());
            if (!product.getIsFreeShipping()) {
                shippingFee = shippingFee.add(product.getShippingFee().multiply(new BigDecimal(item.getAmount())));
            }
        }
        return shippingFee;
    }

    //创建一个和支付有关的内部类
    public class PayDetail {
        String payType; //支付类型
        BigDecimal payPrice;    //微信支付的金额
        BigDecimal balancePaidPrice;    //余额支付的金额

        public PayDetail(Integer userId, BigDecimal totalPrice) {
            //用户余额
            BigDecimal userBalance = userService.findById(userId.toString()).getBalance();

            if (userBalance.compareTo(totalPrice) >= 0) {  //如果余额足够支付，则返回余额支付态
                payType = PinConstants.PayType.WEICHAT;
                payPrice = new BigDecimal("0");
                balancePaidPrice = totalPrice;
            }
            //如果余额不够，并且余额不等于0，则返回余额和微信支付
            else if ((userBalance.compareTo(totalPrice) < 0) && (userBalance.compareTo(new BigDecimal("0")) != 0)) {
                payType = PinConstants.PayType.BOTH;
                payPrice = totalPrice.subtract(userBalance);
                balancePaidPrice = userBalance;

            }
            //没有余额的话，则返回微信支付
            else/* if (userBalance.compareTo(new BigDecimal("0")) == 0)*/ {
                payType = PinConstants.PayType.WEICHAT;
                payPrice = totalPrice;
                balancePaidPrice = new BigDecimal("0");

            }
        }

        public BigDecimal getPayPrice() {
            return payPrice;
        }

        public BigDecimal getBalancePaidPrice() {
            return balancePaidPrice;
        }

        public String getPayType() {
            return payType;
        }
    }


}
