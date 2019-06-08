package cn.edu.neu.shop.pin.service;

import cn.edu.neu.shop.pin.mapper.PinOrderItemMapper;
import cn.edu.neu.shop.pin.mapper.PinProductAttributeValueMapper;
import cn.edu.neu.shop.pin.mapper.PinProductMapper;
import cn.edu.neu.shop.pin.model.*;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author flyhero, ydy
 */
@Service
public class OrderItemService extends AbstractService<PinOrderItem> {

    public static final int STATUS_ADD_ORDER_ITEM_SUCCESS = 0;
    public static final int STATUS_ADD_ORDER_ITEM_INVALID_ID = -1;
    public static final int STATUS_DELETE_ORDER_ITEM_SUCCESS = 0;
    public static final int STATUS_DELETE_ORDER_ITEM_INVALID_ID = -1;
    public static final int STATUS_DELETE_ORDER_ITEM_PERMISSION_DENIED = -2;

    @Autowired
    private PinOrderItemMapper pinOrderItemMapper;

    @Autowired
    private PinProductAttributeValueMapper pinProductAttributeValueMapper;

    @Autowired
    private PinProductMapper pinProductMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private UserRoleListTransferService userService;


    /**
     * TODO:ydy 未测试
     * 通过传进来的JSONArray 产生 PinOrderItem 的array
     *
     * @param array 传入的JSONArray 里面是order_item的id
     * @return 返回由PinOrderItem组成的ArrayList
     */
    public List<PinOrderItem> getItemListByJSONArray(JSONArray array) {
        List<PinOrderItem> list = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            //这个selectByPrimaryKey 不知道有没有用
            list.add(pinOrderItemMapper.selectByPrimaryKey(array.getInteger(i)));
        }
        return list;
    }

    /**
     * TODO:ydy 未测试
     * 通过JSONArray 传入PinOrderItem的数组
     *
     * @param array 数组 里面都是PinOrderItem的对象
     * @return 返回商品总数
     */
    public Integer getProductAmount(List<PinOrderItem> array) {
        Integer amount = 0;
        PinProduct product;
        for (PinOrderItem item : array) {
            amount += item.getAmount();
            product = productService.getProductById(item.getProductId());
            //同时将对应的商品的amount减去相应的数量
            if (product.getStockCount() >= item.getAmount())
                product.setStockCount(product.getStockCount() - item.getAmount());
            else
                //库存不够，返回-1
                return -1;

        }
        return amount;
    }

    /**
     * TODO:ydy 未测试
     * 通过JSONArray 传入PinOrderItem的数组
     *
     * @param array 数组 里面都是PinOrderItem的对象
     * @return 返回商品总数
     */
    public BigDecimal getProductTotalPrice(List<PinOrderItem> array) {
        //BigDecimal 尽量用字符串初始化
        BigDecimal price = new BigDecimal("0");
        for (PinOrderItem item : array) {
            price = price.add(item.getTotalPrice());
        }
        return price;
    }

    /**
     * TODO:ydy 未测试
     *
     * @param array 传入一个PinOrderItem数组
     * @return 返回总的邮费
     */
    public BigDecimal getAllShippingFee(List<PinOrderItem> array) {
        BigDecimal shippingFee = new BigDecimal("0");
        PinProduct product;
        for (PinOrderItem item : array) {
            product = productService.getProductById(item.getProductId());
            if (!product.getIsFreeShipping()) {
                shippingFee = shippingFee.add(product.getShippingFee().multiply(new BigDecimal(item.getAmount())));
            }
        }
        return shippingFee;
    }

    /**
     * TODO:ydy 未测试
     * 传入所有的PinOrderItem的list，计算得到所有的成本
     *
     * @param array PinOrderItem的list
     * @return 成本
     */
    public BigDecimal getTotalCost(List<PinOrderItem> array) {
        BigDecimal total = new BigDecimal("0");
        for (PinOrderItem item : array) {
            total = total.add(item.getTotalCost());
        }
        return total;
    }

    /**
     * 挂载orderItem到OrderIndividual
     *
     * @param orderItemList 所选商品列表
     * @param targetOrderIndividualId 目标ID
     */
    @Transactional
    public void mountOrderItems(List<PinOrderItem> orderItemList, Integer targetOrderIndividualId) {
        for (PinOrderItem item : orderItemList) {
            // 修改提交状态和目标子订单ID
            item.setOrderIndividualId(targetOrderIndividualId);
            item.setIsSubmitted(true);
            update(item);
        }
    }

    /**
     * @author flyhero
     * 添加商品到购物车（新增一条新的OrderItem记录）
     *
     * @param userId
     * @param productId
     * @param skuId
     * @param amount
     */
    @Transactional
    public Integer createOrderItem(Integer userId, Integer productId, Integer skuId, Integer amount) {
        // 查找对应的sku信息
        PinProductAttributeValue p = pinProductAttributeValueMapper.selectByPrimaryKey(skuId);
        PinProduct product = productService.getProductById(productId);
        if (p == null || product == null) return STATUS_ADD_ORDER_ITEM_INVALID_ID;
        // 计算并插入一条OrderItem记录
        BigDecimal totalPrice = p.getPrice().multiply(BigDecimal.valueOf(amount));
        BigDecimal totalCost = p.getCost().multiply(BigDecimal.valueOf(amount));
        PinOrderItem orderItem = new PinOrderItem(userId, productId, skuId, amount, totalPrice, totalCost, null, false);
        if(checkIfAlreadyHaveOrderItem(userId, skuId)) { //当前用户购物车中已有同型号商品，在原来基础上增加数量
            pinOrderItemMapper.addAmountInExistingOrderItem(amount, totalPrice, totalCost, userId, skuId);
        } else { //购物车中没有同型号商品，新增一条OrderItem记录
            save(orderItem);
        }
        return STATUS_ADD_ORDER_ITEM_SUCCESS;
    }

    /**
     * @author flyhero
     * 获取当前用户购物车中所有OrderItem的信息，加上其对应的商品、店铺、sku信息
     *
     * @param userId
     * @return
     */
    public List<PinOrderItem> getAllOrderItems(Integer userId) {
        PinOrderItem pinOrderItem = new PinOrderItem();
        pinOrderItem.setUserId(userId);
        pinOrderItem.setOrderIndividualId(null);
        pinOrderItem.setIsSubmitted(false);
        List<PinOrderItem> list = pinOrderItemMapper.select(pinOrderItem);
        for (PinOrderItem p : list) {
            PinProduct pro = productService.getProductById(p.getProductId());
            PinProductAttributeValue val = pinProductAttributeValueMapper.getSkuBySkuId(p.getSkuId());
            PinStore store = storeService.getStoreById(pro.getStoreId());
            pro.setStore(store);
            p.setProduct(pro);
            p.setProductAttributeValue(val);
        }
        return list;
    }

    /**
     * @author flyhero
     * 删除订单信息
     *
     * @param userId
     * @param orderItemIds
     * @return
     */
    @Transactional
    public Integer deleteOrderItems(Integer userId, List<Integer> orderItemIds) {
        PinUser user = userService.findById(userId);
        for (Integer id : orderItemIds) {
            PinOrderItem orderItem = pinOrderItemMapper.selectByPrimaryKey(id);
            if (id == null) return STATUS_DELETE_ORDER_ITEM_INVALID_ID;
            if (userId != orderItem.getUserId()) return STATUS_DELETE_ORDER_ITEM_PERMISSION_DENIED;
            pinOrderItemMapper.delete(orderItem);
        }
        return STATUS_DELETE_ORDER_ITEM_SUCCESS;
    }

    /**
     * @author flyhero
     * 根据orderIndividualId返回所有的orderItem，集成了product和attributeValue
     *
     * @param orderIndividualId 子订单ID
     * @return 子订单对应的已选商品项目列表
     */
    public List<PinOrderItem> getOrderItemsByOrderIndividualId(Integer orderIndividualId) {
        PinOrderItem orderItem = new PinOrderItem();
        orderItem.setOrderIndividualId(orderIndividualId);
        List<PinOrderItem> list = pinOrderItemMapper.select(orderItem);
        for (PinOrderItem o : list) {
            PinProduct product = pinProductMapper.getProductById(o.getProductId());
            o.setProduct(product);
            PinProductAttributeValue pinProductAttributeValue =
                    pinProductAttributeValueMapper.selectByPrimaryKey(o.getSkuId());
            o.setProductAttributeValue(pinProductAttributeValue);
        }
        return list;
    }

    /**
     * 创建一个和支付有关的内部类
     */
    public class PayDetail {
        String payType; //支付类型
        BigDecimal payPrice;    //微信支付的金额
        BigDecimal balancePaidPrice;    //余额支付的金额

        public PayDetail(Integer userId, BigDecimal totalPrice) {
            //用户余额
            BigDecimal userBalance = userService.findById(userId).getBalance();

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

    /**
     * @author flyhero
     * 私有方法，在新增OrderItem之前判断一下数据库中是否已有同型号商品的添加记录
     * @param userId
     * @param skuId
     * @return
     */
    private Boolean checkIfAlreadyHaveOrderItem(Integer userId, Integer skuId) {
        PinOrderItem poi = new PinOrderItem();
        poi.setUserId(userId);
        poi.setSkuId(skuId);
        PinOrderItem pinOrderItem = pinOrderItemMapper.selectOne(poi);
        return pinOrderItem == null;
    }
}
