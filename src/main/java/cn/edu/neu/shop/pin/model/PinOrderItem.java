package cn.edu.neu.shop.pin.model;

import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "pin_order_item")
public class PinOrderItem {
    /**
     * 订单已选品ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 已选品数量
     */
    private Integer amount;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 商品ID
     */
    @Column(name = "product_id")
    private Integer productId;

    /**
     * sku ID
     */
    @Column(name = "sku_id")
    private Integer skuId;

    /**
     * 商品总价
     */
    @Column(name = "total_price")
    private BigDecimal totalPrice;

    /**
     * 商品总成本
     */
    @Column(name = "total_cost")
    private BigDecimal totalCost;

    /**
     * 个人订单id
     */
    @Column(name = "order_individual_id")
    private Integer orderIndividualId;

    /**
     * 是否已提交结算
     */
    @Column(name = "is_submitted")
    private Boolean isSubmitted;

    private PinProduct pinProduct;

    private PinProductAttributeValue pinProductAttributeValue;

    /**
     * 默认无参构造方法
     */
    public PinOrderItem() {

    }

    /**
     * 默认满参构造方法
     * @param amount
     * @param userId
     * @param productId
     * @param skuId
     * @param totalPrice
     * @param totalCost
     * @param orderIndividualId
     * @param isSubmitted
     */
    public PinOrderItem(Integer amount, Integer userId, Integer productId, Integer skuId, BigDecimal totalPrice, BigDecimal totalCost, Integer orderIndividualId, Boolean isSubmitted) {
        this.amount = amount;
        this.userId = userId;
        this.productId = productId;
        this.skuId = skuId;
        this.totalPrice = totalPrice;
        this.totalCost = totalCost;
        this.orderIndividualId = orderIndividualId;
        this.isSubmitted = isSubmitted;
    }

    /**
     * 获取订单已选品ID
     *
     * @return id - 订单已选品ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置订单已选品ID
     *
     * @param id 订单已选品ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取已选品数量
     *
     * @return amount - 已选品数量
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * 设置已选品数量
     *
     * @param amount 已选品数量
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     * 获取商品总价
     *
     * @return total_price - 商品总价
     */
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    /**
     * 设置商品总价
     *
     * @param totalPrice 商品总价
     */
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * 获取商品总成本
     *
     * @return total_cost - 商品总成本
     */
    public BigDecimal getTotalCost() {
        return totalCost;
    }

    /**
     * 设置商品总成本
     *
     * @param totalCost 商品总成本
     */
    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    /**
     * 获取个人订单id
     *
     * @return order_individual_id - 个人订单id
     */
    public Integer getOrderIndividualId() {
        return orderIndividualId;
    }

    /**
     * 设置个人订单id
     *
     * @param orderIndividualId 个人订单id
     */
    public void setOrderIndividualId(Integer orderIndividualId) {
        this.orderIndividualId = orderIndividualId;
    }

    /**
     * 获取是否已提交结算
     *
     * @return is_submitted - 是否已提交结算
     */
    public Boolean getIsSubmitted() {
        return isSubmitted;
    }

    /**
     * 设置是否已提交结算
     *
     * @param isSubmitted 是否已提交结算
     */
    public void setIsSubmitted(Boolean isSubmitted) {
        this.isSubmitted = isSubmitted;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public Boolean getSubmitted() {
        return isSubmitted;
    }

    public void setSubmitted(Boolean submitted) {
        isSubmitted = submitted;
    }

    public PinProduct getPinProduct() {
        return pinProduct;
    }

    public void setPinProduct(PinProduct pinProduct) {
        this.pinProduct = pinProduct;
    }

    public PinProductAttributeValue getPinProductAttributeValue() {
        return pinProductAttributeValue;
    }

    public void setPinProductAttributeValue(PinProductAttributeValue pinProductAttributeValue) {
        this.pinProductAttributeValue = pinProductAttributeValue;
    }
}