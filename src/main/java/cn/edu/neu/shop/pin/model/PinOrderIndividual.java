package cn.edu.neu.shop.pin.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Table(name = "pin_order_individual")
public class PinOrderIndividual {

    /**
     * 待发货
     */
    public static final int STATUS_DEPENDING_TO_SHIP = 0;
    /**
     * 待收货
     */
    public static final int STATUS_SHIPPED = 1;
    /**
     * 已收货待评价
     */
    public static final int STATUS_PENDING_COMMENT = 2;
    /**
     * 已评价（订单已完成）
     */
    public static final int STATUS_COMMENTED = 3;
    /**
     * 已退款（订单退款关闭）
     */
    public static final int STATUS_REFUND_SUCCESS = 4;
    /**
     * 退款被拒绝
     */
    public static final int STATUS_REFUND_REFUSED = 5;
    /**
     * 订单未申请退款（默认状态）
     */
    public static final int REFUND_STATUS_NOT_APPLIED = 0;
    /**
     * 订单退款申请中
     */
    public static final int REFUND_STATUS_APPLYING = 1;
    /**
     * 订单退款申请状态结束
     */
    public static final int REFUND_STATUS_FINISHED = 2;
    /**
     * 存储其包含的所有OrderItem
     */
    List<PinOrderItem> orderItems;
    /**
     * 订单ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 团订单号
     */
    @Column(name = "order_group_id")
    private Integer orderGroupId;
    /**
     * 商户ID
     */
    @Column(name = "store_id")
    private Integer storeId;
    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Integer userId;
    /**
     * 用户姓名
     */
    @Column(name = "receiver_name")
    private String receiverName;
    /**
     * 用户电话
     */
    @Column(name = "receiver_phone")
    private String receiverPhone;
    /**
     * 详细地址
     */
    @Column(name = "delivery_address")
    private String deliveryAddress;
    /**
     * 订单商品总数
     */
    @Column(name = "total_product_number")
    private Integer totalProductNumber;
    /**
     * 订单总价
     */
    @Column(name = "total_price")
    private BigDecimal totalPrice;
    /**
     * 邮费
     */
    private BigDecimal shippingFee;
    /**
     * 实际支付金额
     */
    @Column(name = "pay_price")
    private BigDecimal payPrice;
    /**
     * 余额支付的金额部分
     */
    @Column(name = "balance_paid_price")
    private BigDecimal balancePaidPrice;
    /**
     * 支付时间
     */
    @Column(name = "pay_time")
    private Date payTime;
    /**
     * 支付状态
     */
    private Boolean paid;
    /**
     * 支付方式; BALANCE 余额 / WECHAT 微信
     */
    @Column(name = "pay_type")
    private String payType;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;
    /**
     * 订单状态订单状态（0：待发货；1：待收货；2：待评价；3：已评价; 4: 已退款; 5: 已拒绝）
     */
    private Integer status;
    /**
     * 0 未退款 1 申请中 2 已退款
     */
    @Column(name = "refund_status")
    private Integer refundStatus;
    /**
     * 退款图片
     */
    @Column(name = "refund_reason_image")
    private String refundReasonImage;
    /**
     * 退款用户说明
     */
    @Column(name = "refund_reason_explain")
    private String refundReasonExplain;
    /**
     * 退款申请时间
     */
    @Column(name = "refund_apply_time")
    private Date refundApplyTime;
    /**
     * 商户填写的不退款的理由
     */
    @Column(name = "refund_refuse_reason")
    private String refundRefuseReason;
    /**
     * 退款金额
     */
    @Column(name = "refund_price")
    private BigDecimal refundPrice;
    /**
     * 快递名称/送货人姓名
     */
    @Column(name = "delivery_name")
    private String deliveryName;
    /**
     * 发货类型
     */
    @Column(name = "delivery_type")
    private String deliveryType;
    /**
     * 快递单号/手机号
     */
    @Column(name = "delivery_id")
    private String deliveryId;
    /**
     * 消费赚取积分
     */
    @Column(name = "gained_credit")
    private Integer gainedCredit;
    /**
     * 商户备注
     */
    @Column(name = "merchant_remark")
    private String merchantRemark;
    /**
     * 管理员备注
     */
    @Column(name = "user_remark")
    private String userRemark;
    /**
     * 是否拼团
     */
    @Column(name = "is_group")
    private Boolean isGroup;
    /**
     * 总成本价
     */
    @Column(name = "total_cost")
    private BigDecimal totalCost;

    private PinStore store;

    public PinOrderIndividual() {
        super();
    }

    public PinOrderIndividual(Integer orderGroupId, Integer storeId, Integer userId, String receiverName, String receiverPhone, String deliveryAddress, Integer totalProductNumber, BigDecimal totalPrice, BigDecimal shippingFee, BigDecimal payPrice, BigDecimal balancePaidPrice, Date payTime, Boolean paid, String payType, Date createTime, Integer status, Integer refundStatus, String refundReasonImage, String refundReasonExplain, Date refundApplyTime, String refundRefuseReason, BigDecimal refundPrice, String deliveryName, String deliveryType, String deliveryId, Integer gainedCredit, String merchantRemark, String userRemark, Boolean isGroup, BigDecimal totalCost) {
        this.orderGroupId = orderGroupId;
        this.storeId = storeId;
        this.userId = userId;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.deliveryAddress = deliveryAddress;
        this.totalProductNumber = totalProductNumber;
        this.totalPrice = totalPrice;
        this.shippingFee = shippingFee;
        this.payPrice = payPrice;
        this.balancePaidPrice = balancePaidPrice;
        this.payTime = payTime;
        this.paid = paid;
        this.payType = payType;
        this.createTime = createTime;
        this.status = status;
        this.refundStatus = refundStatus;
        this.refundReasonImage = refundReasonImage;
        this.refundReasonExplain = refundReasonExplain;
        this.refundApplyTime = refundApplyTime;
        this.refundRefuseReason = refundRefuseReason;
        this.refundPrice = refundPrice;
        this.deliveryName = deliveryName;
        this.deliveryType = deliveryType;
        this.deliveryId = deliveryId;
        this.gainedCredit = gainedCredit;
        this.merchantRemark = merchantRemark;
        this.userRemark = userRemark;
        this.isGroup = isGroup;
        this.totalCost = totalCost;
    }

    /**
     * 获取订单ID
     *
     * @return id - 订单ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置订单ID
     *
     * @param id 订单ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取团订单号
     *
     * @return order_group_id - 团订单号
     */
    public Integer getOrderGroupId() {
        return orderGroupId;
    }

    /**
     * 设置团订单号
     *
     * @param orderGroupId 团订单号
     */
    public void setOrderGroupId(Integer orderGroupId) {
        this.orderGroupId = orderGroupId;
    }

    /**
     * 获取商户ID
     *
     * @return store_id - 商户ID
     */
    public Integer getStoreId() {
        return storeId;
    }

    /**
     * 设置商户ID
     *
     * @param storeId 商户ID
     */
    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    /**
     * 获取用户id
     *
     * @return user_id - 用户id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户id
     *
     * @param userId 用户id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取用户姓名
     *
     * @return receiver_name - 用户姓名
     */
    public String getReceiverName() {
        return receiverName;
    }

    /**
     * 设置用户姓名
     *
     * @param receiverName 用户姓名
     */
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    /**
     * 获取用户电话
     *
     * @return receiver_phone - 用户电话
     */
    public String getReceiverPhone() {
        return receiverPhone;
    }

    /**
     * 设置用户电话
     *
     * @param receiverPhone 用户电话
     */
    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    /**
     * 获取详细地址
     *
     * @return delivery_address - 详细地址
     */
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    /**
     * 设置详细地址
     *
     * @param deliveryAddress 详细地址
     */
    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    /**
     * 获取订单商品总数
     *
     * @return total_product_number - 订单商品总数
     */
    public Integer getTotalProductNumber() {
        return totalProductNumber;
    }

    /**
     * 设置订单商品总数
     *
     * @param totalProductNumber 订单商品总数
     */
    public void setTotalProductNumber(Integer totalProductNumber) {
        this.totalProductNumber = totalProductNumber;
    }

    /**
     * 获取订单总价
     *
     * @return total_price - 订单总价
     */
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    /**
     * 设置订单总价
     *
     * @param totalPrice 订单总价
     */
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * 获取邮费
     *
     * @return shippingFee - 邮费
     */
    public BigDecimal getShippingFee() {
        return shippingFee;
    }

    /**
     * 设置邮费
     *
     * @param shippingFee 邮费
     */
    public void setShippingFee(BigDecimal shippingFee) {
        this.shippingFee = shippingFee;
    }

    /**
     * 获取实际支付金额
     *
     * @return pay_price - 实际支付金额
     */
    public BigDecimal getPayPrice() {
        return payPrice;
    }

    /**
     * 设置实际支付金额
     *
     * @param payPrice 实际支付金额
     */
    public void setPayPrice(BigDecimal payPrice) {
        this.payPrice = payPrice;
    }

    /**
     * 获取余额支付的金额部分
     *
     * @return balance_paid_price - 余额支付的金额部分
     */
    public BigDecimal getBalancePaidPrice() {
        return balancePaidPrice;
    }

    /**
     * 设置余额支付的金额部分
     *
     * @param balancePaidPrice 余额支付的金额部分
     */
    public void setBalancePaidPrice(BigDecimal balancePaidPrice) {
        this.balancePaidPrice = balancePaidPrice;
    }

    /**
     * 获取支付时间
     *
     * @return pay_time - 支付时间
     */
    public Date getPayTime() {
        return payTime;
    }

    /**
     * 设置支付时间
     *
     * @param payTime 支付时间
     */
    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    /**
     * 获取支付状态
     *
     * @return paid - 支付状态
     */
    public Boolean getPaid() {
        return paid;
    }

    /**
     * 设置支付状态
     *
     * @param paid 支付状态
     */
    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    /**
     * 获取支付方式; BALANCE 余额 / WECHAT 微信
     *
     * @return pay_type - 支付方式; BALANCE 余额 / WECHAT 微信
     */
    public String getPayType() {
        return payType;
    }

    /**
     * 设置支付方式; BALANCE 余额 / WECHAT 微信
     *
     * @param payType 支付方式; BALANCE 余额 / WECHAT 微信
     */
    public void setPayType(String payType) {
        this.payType = payType;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取订单状态（-1 : 申请退款中 -2 : 退货成功 0：待发货；1：待收货；2：已收货；3：待评价; 4: 已评价）
     *
     * @return status - 订单状态（-1 : 申请退款中 -2 : 退货成功 0：待发货；1：待收货；2：已收货；3：待评价; 4: 已评价）
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置订单状态（-1 : 申请退款中 -2 : 退货成功 0：待发货；1：待收货；2：已收货；3：待评价; 4: 已评价）
     *
     * @param status 订单状态（-1 : 申请退款中 -2 : 退货成功 0：待发货；1：待收货；2：已收货；3：待评价; 4: 已评价）
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取0 未退款 1 申请中 2 已退款
     *
     * @return refund_status - 0 未退款 1 申请中 2 已退款
     */
    public Integer getRefundStatus() {
        return refundStatus;
    }

    /**
     * 设置0 未退款 1 申请中 2 已退款
     *
     * @param refundStatus 0 未退款 1 申请中 2 已退款
     */
    public void setRefundStatus(Integer refundStatus) {
        this.refundStatus = refundStatus;
    }

    /**
     * 获取退款图片
     *
     * @return refund_reason_image - 退款图片
     */
    public String getRefundReasonImage() {
        return refundReasonImage;
    }

    /**
     * 设置退款图片
     *
     * @param refundReasonImage 退款图片
     */
    public void setRefundReasonImage(String refundReasonImage) {
        this.refundReasonImage = refundReasonImage;
    }

    /**
     * 获取退款用户说明
     *
     * @return refund_reason_explain - 退款用户说明
     */
    public String getRefundReasonExplain() {
        return refundReasonExplain;
    }

    /**
     * 设置退款用户说明
     *
     * @param refundReasonExplain 退款用户说明
     */
    public void setRefundReasonExplain(String refundReasonExplain) {
        this.refundReasonExplain = refundReasonExplain;
    }

    /**
     * 获取退款申请时间
     *
     * @return refund_apply_time - 退款申请时间
     */
    public Date getRefundApplyTime() {
        return refundApplyTime;
    }

    /**
     * 设置退款申请时间
     *
     * @param refundApplyTime 退款申请时间
     */
    public void setRefundApplyTime(Date refundApplyTime) {
        this.refundApplyTime = refundApplyTime;
    }

    /**
     * 获取商户填写的不退款的理由
     *
     * @return refund_refuse_reason - 商户填写的不退款的理由
     */
    public String getRefundRefuseReason() {
        return refundRefuseReason;
    }

    /**
     * 设置商户填写的不退款的理由
     *
     * @param refundRefuseReason 商户填写的不退款的理由
     */
    public void setRefundRefuseReason(String refundRefuseReason) {
        this.refundRefuseReason = refundRefuseReason;
    }

    /**
     * 获取退款金额
     *
     * @return refund_price - 退款金额
     */
    public BigDecimal getRefundPrice() {
        return refundPrice;
    }

    /**
     * 设置退款金额
     *
     * @param refundPrice 退款金额
     */
    public void setRefundPrice(BigDecimal refundPrice) {
        this.refundPrice = refundPrice;
    }

    /**
     * 获取快递名称/送货人姓名
     *
     * @return delivery_name - 快递名称/送货人姓名
     */
    public String getDeliveryName() {
        return deliveryName;
    }

    /**
     * 设置快递名称/送货人姓名
     *
     * @param deliveryName 快递名称/送货人姓名
     */
    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    /**
     * 获取发货类型
     *
     * @return delivery_type - 发货类型
     */
    public String getDeliveryType() {
        return deliveryType;
    }

    /**
     * 设置发货类型
     *
     * @param deliveryType 发货类型
     */
    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    /**
     * 获取快递单号/手机号
     *
     * @return delivery_id - 快递单号/手机号
     */
    public String getDeliveryId() {
        return deliveryId;
    }

    /**
     * 设置快递单号/手机号
     *
     * @param deliveryId 快递单号/手机号
     */
    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    /**
     * 获取消费赚取积分
     *
     * @return gained_credit - 消费赚取积分
     */
    public Integer getGainedCredit() {
        return gainedCredit;
    }

    /**
     * 设置消费赚取积分
     *
     * @param gainedCredit 消费赚取积分
     */
    public void setGainedCredit(Integer gainedCredit) {
        this.gainedCredit = gainedCredit;
    }

    /**
     * 获取商户备注
     *
     * @return merchant_remark - 商户备注
     */
    public String getMerchantRemark() {
        return merchantRemark;
    }

    /**
     * 设置商户备注
     *
     * @param merchantRemark 商户备注
     */
    public void setMerchantRemark(String merchantRemark) {
        this.merchantRemark = merchantRemark;
    }

    /**
     * 获取管理员备注
     *
     * @return user_remark - 管理员备注
     */
    public String getUserRemark() {
        return userRemark;
    }

    /**
     * 设置管理员备注
     *
     * @param userRemark 管理员备注
     */
    public void setUserRemark(String userRemark) {
        this.userRemark = userRemark;
    }

    /**
     * 获取是否拼团
     *
     * @return is_group - 是否拼团
     */
    public Boolean getIsGroup() {
        return isGroup;
    }

    /**
     * 设置是否拼团
     *
     * @param isGroup 是否拼团
     */
    public void setIsGroup(Boolean isGroup) {
        this.isGroup = isGroup;
    }

    /**
     * 获取总成本价
     *
     * @return total_cost - 总成本价
     */
    public BigDecimal getTotalCost() {
        return totalCost;
    }

    /**
     * 设置总成本价
     *
     * @param totalCost 总成本价
     */
    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public Boolean getGroup() {
        return isGroup;
    }

    public void setGroup(Boolean group) {
        isGroup = group;
    }

    public List<PinOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<PinOrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public PinStore getStore() {
        return store;
    }

    public void setStore(PinStore store) {
        this.store = store;
    }

    @Override
    public String toString() {
        return "PinOrderIndividual{" +
                "id=" + id +
                ", orderGroupId=" + orderGroupId +
                ", storeId=" + storeId +
                ", userId=" + userId +
                ", receiverName='" + receiverName + '\'' +
                ", receiverPhone='" + receiverPhone + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", totalProductNumber=" + totalProductNumber +
                ", totalPrice=" + totalPrice +
                ", shippingFee=" + shippingFee +
                ", payPrice=" + payPrice +
                ", balancePaidPrice=" + balancePaidPrice +
                ", payTime=" + payTime +
                ", paid=" + paid +
                ", payType='" + payType + '\'' +
                ", createTime=" + createTime +
                ", status=" + status +
                ", refundStatus=" + refundStatus +
                ", refundReasonImage='" + refundReasonImage + '\'' +
                ", refundReasonExplain='" + refundReasonExplain + '\'' +
                ", refundApplyTime=" + refundApplyTime +
                ", refundRefuseReason='" + refundRefuseReason + '\'' +
                ", refundPrice=" + refundPrice +
                ", deliveryName='" + deliveryName + '\'' +
                ", deliveryType='" + deliveryType + '\'' +
                ", deliveryId='" + deliveryId + '\'' +
                ", gainedCredit=" + gainedCredit +
                ", merchantRemark='" + merchantRemark + '\'' +
                ", userRemark='" + userRemark + '\'' +
                ", isGroup=" + isGroup +
                ", totalCost=" + totalCost +
                '}';
    }
}