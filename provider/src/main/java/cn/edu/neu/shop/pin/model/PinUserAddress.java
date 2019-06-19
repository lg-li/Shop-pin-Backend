package cn.edu.neu.shop.pin.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "pin_user_address")
public class PinUserAddress {
    /**
     * 用户地址id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 收货人姓名
     */
    @Column(name = "real_name")
    private String realName;

    /**
     * 收货人电话
     */
    private String phone;

    /**
     * 收货人所在省
     */
    private String province;

    /**
     * 收货人所在市
     */
    private String city;

    /**
     * 收货人所在区或县
     */
    private String district;

    /**
     * 收货人详细地址
     */
    private String detail;

    /**
     * 邮编
     */
    @Column(name = "post_code")
    private Integer postCode;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 是否为默认地址
     */
    @Column(name = "is_default")
    private Boolean isDefault;

    /**
     * 获取用户地址id
     *
     * @return id - 用户地址id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置用户地址id
     *
     * @param id 用户地址id
     */
    public void setId(Integer id) {
        this.id = id;
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
     * 获取收货人姓名
     *
     * @return real_name - 收货人姓名
     */
    public String getRealName() {
        return realName;
    }

    /**
     * 设置收货人姓名
     *
     * @param realName 收货人姓名
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * 获取收货人电话
     *
     * @return phone - 收货人电话
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置收货人电话
     *
     * @param phone 收货人电话
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取收货人所在省
     *
     * @return province - 收货人所在省
     */
    public String getProvince() {
        return province;
    }

    /**
     * 设置收货人所在省
     *
     * @param province 收货人所在省
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * 获取收货人所在市
     *
     * @return city - 收货人所在市
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置收货人所在市
     *
     * @param city 收货人所在市
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 获取收货人所在区或县
     *
     * @return district - 收货人所在区或县
     */
    public String getDistrict() {
        return district;
    }

    /**
     * 设置收货人所在区或县
     *
     * @param district 收货人所在区或县
     */
    public void setDistrict(String district) {
        this.district = district;
    }

    /**
     * 获取收货人详细地址
     *
     * @return detail - 收货人详细地址
     */
    public String getDetail() {
        return detail;
    }

    /**
     * 设置收货人详细地址
     *
     * @param detail 收货人详细地址
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     * 获取邮编
     *
     * @return post_code - 邮编
     */
    public Integer getPostCode() {
        return postCode;
    }

    /**
     * 设置邮编
     *
     * @param postCode 邮编
     */
    public void setPostCode(Integer postCode) {
        this.postCode = postCode;
    }

    /**
     * 获取经度
     *
     * @return longitude - 经度
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * 设置经度
     *
     * @param longitude 经度
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * 获取纬度
     *
     * @return latitude - 纬度
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * 设置纬度
     *
     * @param latitude 纬度
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
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

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }
}