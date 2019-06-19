package cn.edu.neu.shop.pin.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "pin_store")
public class PinStore {
    /**
     * 店铺id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 店铺名称
     */
    private String name;

    /**
     * 店铺描述
     */
    private String description;

    /**
     * 店主电话号
     */
    private String phone;

    /**
     * 店主电子邮件
     */
    private String email;

    /**
     * 店铺创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 店铺logo链接
     */
    @Column(name = "logo_url")
    private String logoUrl;

    /**
     * 店铺创建者用户id
     */
    @Column(name = "owner_user_id")
    private Integer ownerUserId;

    @Column(name = "people_limit")
    private Integer peopleLimit;

    /**
     * 获取店铺id
     *
     * @return id - 店铺id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置店铺id
     *
     * @param id 店铺id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取店铺名称
     *
     * @return name - 店铺名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置店铺名称
     *
     * @param name 店铺名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取店铺描述
     *
     * @return description - 店铺描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置店铺描述
     *
     * @param description 店铺描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取店主电话号
     *
     * @return phone - 店主电话号
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置店主电话号
     *
     * @param phone 店主电话号
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取店主电子邮件
     *
     * @return email - 店主电子邮件
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置店主电子邮件
     *
     * @param email 店主电子邮件
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取店铺创建时间
     *
     * @return create_time - 店铺创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置店铺创建时间
     *
     * @param createTime 店铺创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取店铺logo链接
     *
     * @return logo_url - 店铺logo链接
     */
    public String getLogoUrl() {
        return logoUrl;
    }

    /**
     * 设置店铺logo链接
     *
     * @param logoUrl 店铺logo链接
     */
    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    /**
     * 获取店铺创建者用户id
     *
     * @return owner_user_id - 店铺创建者用户id
     */
    public Integer getOwnerUserId() {
        return ownerUserId;
    }

    /**
     * 设置店铺创建者用户id
     *
     * @param ownerUserId 店铺创建者用户id
     */
    public void setOwnerUserId(Integer ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public Integer getPeopleLimit() {
        return peopleLimit;
    }

    public void setPeopleLimit(Integer peopleLimit) {
        this.peopleLimit = peopleLimit;
    }

    @Override
    public String toString() {
        return "PinStore{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", createTime=" + createTime +
                ", logoUrl='" + logoUrl + '\'' +
                ", ownerUserId=" + ownerUserId +
                '}';
    }
}