package cn.edu.neu.shop.pin.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "pin_user_balance_record")
public class PinUserBalanceRecord {
    public static final int TYPE_RETURN_BONUS = 0;
    public static final int TYPE_DISCOUNT_ON_ORDER = 1;
    public static final int TYPE_TOP_UP = 2;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 充值用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 个人订单号
     */
    @Column(name = "order_individual_id")
    private Integer orderIndividualId;

    /**
     * 动账金额
     */
    private BigDecimal changedAmount;

    /**
     * 类型, 0: 返现, 1: 抵扣; 2: 充值
     */
    private Integer type;

    /**
     * 动账时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取充值用户id
     *
     * @return user_id - 充值用户id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置充值用户id
     *
     * @param userId 充值用户id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取个人订单号
     *
     * @return order_individual_id - 个人订单号
     */
    public Integer getOrderIndividualId() {
        return orderIndividualId;
    }

    /**
     * 设置个人订单号
     *
     * @param orderIndividualId 个人订单号
     */
    public void setOrderIndividualId(Integer orderIndividualId) {
        this.orderIndividualId = orderIndividualId;
    }

    /**
     * 获取动账金额
     *
     * @return changedAmount - 动账金额
     */
    public BigDecimal getChangedAmount() {
        return changedAmount;
    }

    /**
     * 设置动账金额
     *
     * @param changedAmount 动账金额
     */
    public void setChangedAmount(BigDecimal changedAmount) {
        this.changedAmount = changedAmount;
    }

    /**
     * 获取类型, 0: 返现, 1: 抵扣; 2: 充值
     *
     * @return type - 类型, 0: 返现, 1: 抵扣; 2: 充值
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置类型, 0: 返现, 1: 抵扣; 2: 充值
     *
     * @param type 类型, 0: 返现, 1: 抵扣; 2: 充值
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取动账时间
     *
     * @return create_time - 动账时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置动账时间
     *
     * @param createTime 动账时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}