package cn.edu.neu.shop.pin.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "pin_user_credit_record")
public class PinUserCreditRecord {
    public static final int TYPE_FROM_CHECK_IN = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 积分变动用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 动账积分变化（区分正负）
     */
    @Column(name = "value_change")
    private Integer valueChange;

    /**
     * 动账类型, 0: 签到, 1: 购买商品; 2: 抵扣
     */
    private Integer type;

    /**
     * 动账时间
     */
    @Column(name = "create_time")
    private Date createTime;

    private Integer note;

    public PinUserCreditRecord() {
        super();
    }

    public PinUserCreditRecord(Integer userId, Integer valueChange, Integer type, Date createTime, Integer note) {
        this.userId = userId;
        this.valueChange = valueChange;
        this.type = type;
        this.createTime = createTime;
        this.note = note;
    }

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
     * 获取积分变动用户id
     *
     * @return user_id - 积分变动用户id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置积分变动用户id
     *
     * @param userId 积分变动用户id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取动账积分变化（区分正负）
     *
     * @return value_change - 动账积分变化（区分正负）
     */
    public Integer getValueChange() {
        return valueChange;
    }

    /**
     * 设置动账积分变化（区分正负）
     *
     * @param valueChange 动账积分变化（区分正负）
     */
    public void setValueChange(Integer valueChange) {
        this.valueChange = valueChange;
    }

    /**
     * 获取动账类型, 0: 签到, 1: 购买商品; 2: 抵扣
     *
     * @return type - 动账类型, 0: 签到, 1: 购买商品; 2: 抵扣
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置动账类型, 0: 签到, 1: 购买商品; 2: 抵扣
     *
     * @param type 动账类型, 0: 签到, 1: 购买商品; 2: 抵扣
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

    public Integer getNote() {
        return note;
    }

    public void setNote(Integer note) {
        this.note = note;
    }
}