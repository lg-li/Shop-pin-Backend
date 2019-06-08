package cn.edu.neu.shop.pin.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "pin_order_group")
public class PinOrderGroup {
    /**
     * 拼团单ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "owner_user_id")
    private Integer ownerUserId;

    @Column(name = "store_id")
    private Integer storeId;

    /**
     * 0: 正在拼团；1：已结束拼团
     */
    private Boolean status;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "close_time")
    private Date closeTime;

    @Column(name = "actual_finish_time")
    private Date actualFinishTime;

    @Column(name = "total_amount_of_money_paid")
    private BigDecimal totalAmountOfMoneyPaid;

    private PinUser pinUser;

    /**
     * 获取拼团单ID
     *
     * @return id - 拼团单ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置拼团单ID
     *
     * @param id 拼团单ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return owner_user_id
     */
    public Integer getOwnerUserId() {
        return ownerUserId;
    }

    /**
     * @param ownerUserId
     */
    public void setOwnerUserId(Integer ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    /**
     * @return store_id
     */
    public Integer getStoreId() {
        return storeId;
    }

    /**
     * @param storeId
     */
    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    /**
     * 获取0: 正在拼团；1：已结束拼团
     *
     * @return status - 0: 正在拼团；1：已结束拼团
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * 设置0: 正在拼团；1：已结束拼团
     *
     * @param status 0: 正在拼团；1：已结束拼团
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return close_time
     */
    public Date getCloseTime() {
        return closeTime;
    }

    /**
     * @param closeTime
     */
    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    /**
     * @return actual_finish_time
     */
    public Date getActualFinishTime() {
        return actualFinishTime;
    }

    /**
     * @param actualFinishTime
     */
    public void setActualFinishTime(Date actualFinishTime) {
        this.actualFinishTime = actualFinishTime;
    }

    /**
     * @return total_amount_of_money_paid
     */
    public BigDecimal getTotalAmountOfMoneyPaid() {
        return totalAmountOfMoneyPaid;
    }

    /**
     * @param totalAmountOfMoneyPaid
     */
    public void setTotalAmountOfMoneyPaid(BigDecimal totalAmountOfMoneyPaid) {
        this.totalAmountOfMoneyPaid = totalAmountOfMoneyPaid;
    }

    public PinUser getPinUser() {
        return pinUser;
    }

    public void setPinUser(PinUser pinUser) {
        this.pinUser = pinUser;
    }
}