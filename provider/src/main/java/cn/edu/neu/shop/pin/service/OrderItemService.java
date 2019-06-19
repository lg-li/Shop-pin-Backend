package cn.edu.neu.shop.pin.service;

import cn.edu.neu.shop.pin.exception.InvalidOperationException;
import cn.edu.neu.shop.pin.exception.PermissionDeniedException;
import cn.edu.neu.shop.pin.exception.RecordNotFoundException;
import cn.edu.neu.shop.pin.mapper.PinOrderItemMapper;
import cn.edu.neu.shop.pin.mapper.PinProductAttributeValueMapper;
import cn.edu.neu.shop.pin.mapper.PinProductMapper;
import cn.edu.neu.shop.pin.model.PinOrderItem;
import cn.edu.neu.shop.pin.model.PinProduct;
import cn.edu.neu.shop.pin.model.PinProductAttributeValue;
import cn.edu.neu.shop.pin.model.PinStore;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import com.alibaba.fastjson.JSONArray;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    private final PinOrderItemMapper pinOrderItemMapper;

    private final PinProductAttributeValueMapper pinProductAttributeValueMapper;

    private final PinProductMapper pinProductMapper;

    private final ProductService productService;

    private final StoreService storeService;

    public OrderItemService(PinOrderItemMapper pinOrderItemMapper, PinProductAttributeValueMapper pinProductAttributeValueMapper, PinProductMapper pinProductMapper, ProductService productService, StoreService storeService) {
        this.pinOrderItemMapper = pinOrderItemMapper;
        this.pinProductAttributeValueMapper = pinProductAttributeValueMapper;
        this.pinProductMapper = pinProductMapper;
        this.productService = productService;
        this.storeService = storeService;
    }


    /**
     * ydy 未测试
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
     * ydy 未测试
     * 通过JSONArray 传入PinOrderItem的数组
     *
     * @param array 数组 里面都是PinOrderItem的对象
     * @return 返回商品总数
     */
    Integer getProductAmount(List<PinOrderItem> array) {
        Integer amount = 0;
        PinProduct product;
        for (PinOrderItem item : array) {
            amount += item.getAmount();
            product = productService.getProductById(item.getProductId());
            //同时将对应的商品的amount减去相应的数量
            if (product.getStockCount() >= item.getAmount()) {
                product.setStockCount(product.getStockCount() - item.getAmount());
            } else {
                //库存不够，返回-1
                return -1;
            }
        }
        return amount;
    }

    /**
     * ydy 未测试
     * 通过JSONArray 传入PinOrderItem的数组
     *
     * @param array 数组 里面都是PinOrderItem的对象
     * @return 返回商品总数
     */
    BigDecimal getProductTotalPrice(List<PinOrderItem> array) {
        //BigDecimal 尽量用字符串初始化
        BigDecimal price = new BigDecimal("0");
        for (PinOrderItem item : array) {
            price = price.add(item.getTotalPrice());
        }
        return price;
    }

    /**
     * ydy 未测试
     *
     * @param array 传入一个PinOrderItem数组
     * @return 返回总的邮费
     */
    BigDecimal getAllShippingFee(List<PinOrderItem> array) {
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
     * ydy 未测试
     * 传入所有的PinOrderItem的list，计算得到所有的成本
     *
     * @param array PinOrderItem的list
     * @return 成本
     */
    BigDecimal getTotalCost(List<PinOrderItem> array) {
        BigDecimal total = new BigDecimal("0");
        for (PinOrderItem item : array) {
            total = total.add(item.getTotalCost());
        }
        return total;
    }

    /**
     * 挂载orderItem到OrderIndividual
     *
     * @param orderItemList           所选商品列表
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
     * @param userId 用户ID
     * @return 购物车信息
     * @author flyhero
     * 获取当前用户购物车中所有OrderItem的信息，加上其对应的商品、店铺、sku信息
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
     * @param userId    用户ID
     * @param productId 商品ID
     * @param skuId     skuID
     * @param amount    商品购买数量
     * @author flyhero
     * 添加商品到购物车（新增一条新的OrderItem记录）
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
        if (checkIfAlreadyHaveOrderItem(userId, skuId)) { //当前用户购物车中已有同型号商品，在原来基础上增加数量
            pinOrderItemMapper.addAmountInExistingOrderItem(amount, totalPrice, totalCost, userId, skuId);
        } else { //购物车中没有同型号商品，新增一条OrderItem记录
            pinOrderItemMapper.insert(orderItem);
        }
        return STATUS_ADD_ORDER_ITEM_SUCCESS;
    }

    /**
     * @param userId       用户ID
     * @param orderItemIds 一条购物车记录ID
     * @return 状态码
     * @author flyhero
     * 删除订单信息
     */
    @Transactional
    public Integer deleteOrderItems(Integer userId, List<Integer> orderItemIds) {
        for (Integer id : orderItemIds) {
            PinOrderItem orderItem = pinOrderItemMapper.selectByPrimaryKey(id);
            if (id == null) return STATUS_DELETE_ORDER_ITEM_INVALID_ID;
            if (!Objects.equals(userId, orderItem.getUserId())) return STATUS_DELETE_ORDER_ITEM_PERMISSION_DENIED;
            pinOrderItemMapper.delete(orderItem);
        }
        return STATUS_DELETE_ORDER_ITEM_SUCCESS;
    }

    /**
     * @param orderIndividualId 子订单ID
     * @return 子订单对应的已选商品项目列表
     * @author flyhero
     * 根据orderIndividualId返回所有的orderItem，集成了product和attributeValue
     */
    List<PinOrderItem> getOrderItemsByOrderIndividualId(Integer orderIndividualId) {
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
     * @param userId      用户ID
     * @param orderItemId 购物车记录ID
     * @param amount      商品数量
     * @throws PermissionDeniedException 权限不够
     * @throws RecordNotFoundException   记录未找到
     */
    public void changeOrderItemAmount(Integer userId, Integer orderItemId, Integer amount) throws PermissionDeniedException, RecordNotFoundException, InvalidOperationException {
        PinOrderItem orderItem = this.findById(orderItemId);
        if (orderItem == null) {
            throw new RecordNotFoundException("Caused by OrderItemService.changeOrderItemAmount: OrderItem记录不存在！");
        } else if (!Objects.equals(userId, orderItem.getUserId())) {
            throw new PermissionDeniedException("Caused by OrderItemService.changeOrderItemAmount: 用户ID不符");
        } else if (amount < 1) {
            throw new InvalidOperationException("Caused by OrderItemService.changeOrderItemAmount: 购买数量不能小于1！");
        }
        PinProductAttributeValue ppav = pinProductAttributeValueMapper.selectByPrimaryKey(orderItem.getSkuId());
        // 更新总价和成本价
        orderItem.setTotalPrice(ppav.getPrice().multiply(BigDecimal.valueOf(amount)));
        orderItem.setTotalCost(ppav.getCost().multiply(BigDecimal.valueOf(amount)));
        orderItem.setAmount(amount);
        this.update(orderItem);
    }

//    /**
//     * 创建一个和支付有关的内部类
//     */
//    public class PayDetail {
//        String payType; //支付类型
//        BigDecimal payPrice;    //微信支付的金额
//        BigDecimal balancePaidPrice;    //余额支付的金额
//
//        public PayDetail(Integer userId, BigDecimal totalPrice) {
//            //用户余额
//            BigDecimal userBalance = userService.findById(userId).getBalance();
//
//            if (userBalance.compareTo(totalPrice) >= 0) {  //如果余额足够支付，则返回余额支付态
//                payType = PinConstants.PayType.WECHAT;
//                payPrice = new BigDecimal("0");
//                balancePaidPrice = totalPrice;
//            }
//            //如果余额不够，并且余额不等于0，则返回余额和微信支付
//            else if ((userBalance.compareTo(totalPrice) < 0) && (userBalance.compareTo(new BigDecimal("0")) != 0)) {
//                payType = PinConstants.PayType.BOTH;
//                payPrice = totalPrice.subtract(userBalance);
//                balancePaidPrice = userBalance;
//
//            }
//            //没有余额的话，则返回微信支付
//            else/* if (userBalance.compareTo(new BigDecimal("0")) == 0)*/ {
//                payType = PinConstants.PayType.WECHAT;
//                payPrice = totalPrice;
//                balancePaidPrice = new BigDecimal("0");
//
//            }
//        }
//
//        public BigDecimal getPayPrice() {
//            return payPrice;
//        }
//
//        public BigDecimal getBalancePaidPrice() {
//            return balancePaidPrice;
//        }
//
//        public String getPayType() {
//            return payType;
//        }
//    }

    /**
     * @param userId 用户ID
     * @param skuId  skuID
     * @return 是否已有同型号商品在购物车中
     * @author flyhero
     * 私有方法，在新增OrderItem之前判断一下数据库中是否已有同型号商品的添加记录
     */
    private Boolean checkIfAlreadyHaveOrderItem(Integer userId, Integer skuId) {
        PinOrderItem poi = new PinOrderItem();
        poi.setUserId(userId);
        poi.setSkuId(skuId);
        poi.setSubmitted(false);
        PinOrderItem pinOrderItem = pinOrderItemMapper.selectOne(poi);
        return pinOrderItem != null;
    }
}
