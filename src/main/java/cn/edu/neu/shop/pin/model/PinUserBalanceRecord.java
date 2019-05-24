package cn.edu.neu.shop.pin.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "pin_user_balance_record")
public class PinUserBalanceRecord {
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
    private BigDecimal price;

    /**
     * 类型, 0: 返现, 1: 抵扣; 2: 充值
     */
    private Boolean type;

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
     * @return price - 动账金额
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置动账金额
     *
     * @param price 动账金额
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取类型, 0: 返现, 1: 抵扣; 2: 充值
     *
     * @return type - 类型, 0: 返现, 1: 抵扣; 2: 充值
     */
    public Boolean getType() {
        return type;
    }

    /**
     * 设置类型, 0: 返现, 1: 抵扣; 2: 充值
     *
     * @param type 类型, 0: 返现, 1: 抵扣; 2: 充值
     */
    public void setType(Boolean type) {
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