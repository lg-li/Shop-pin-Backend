package cn.edu.neu.shop.pin.model;

import javax.persistence.*;

@Table(name = "pin_settings_express")
public class PinSettingsExpress {
    /**
     * 快递公司id
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 快递公司搜索代码
     */
    private String code;

    /**
     * 快递公司名
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否激活（是否向商家显示）
     */
    @Column(name = "is_activated")
    private Boolean isActivated;

    /**
     * 获取快递公司id
     *
     * @return id - 快递公司id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置快递公司id
     *
     * @param id 快递公司id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取快递公司搜索代码
     *
     * @return code - 快递公司搜索代码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置快递公司搜索代码
     *
     * @param code 快递公司搜索代码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取快递公司名
     *
     * @return name - 快递公司名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置快递公司名
     *
     * @param name 快递公司名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取排序
     *
     * @return sort - 排序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置排序
     *
     * @param sort 排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 获取是否激活（是否向商家显示）
     *
     * @return is_activated - 是否激活（是否向商家显示）
     */
    public Boolean getIsActivated() {
        return isActivated;
    }

    /**
     * 设置是否激活（是否向商家显示）
     *
     * @param isActivated 是否激活（是否向商家显示）
     */
    public void setIsActivated(Boolean isActivated) {
        this.isActivated = isActivated;
    }
}