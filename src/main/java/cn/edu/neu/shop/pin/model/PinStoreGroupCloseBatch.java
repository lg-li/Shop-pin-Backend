package cn.edu.neu.shop.pin.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "pin_store_group_close_batch")
public class PinStoreGroupCloseBatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 店铺id
     */
    @Column(name = "store_id")
    private Integer storeId;

    /**
     * 收团时间（一个店可设置多个）
     */
    private Date time;

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
     * 获取店铺id
     *
     * @return store_id - 店铺id
     */
    public Integer getStoreId() {
        return storeId;
    }

    /**
     * 设置店铺id
     *
     * @param storeId 店铺id
     */
    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    /**
     * 获取收团时间（一个店可设置多个）
     *
     * @return time - 收团时间（一个店可设置多个）
     */
    public Date getTime() {
        return time;
    }

    /**
     * 设置收团时间（一个店可设置多个）
     *
     * @param time 收团时间（一个店可设置多个）
     */
    public void setTime(Date time) {
        this.time = time;
    }
}