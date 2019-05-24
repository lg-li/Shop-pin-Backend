package cn.edu.neu.shop.pin.model;

import javax.persistence.*;

@Table(name = "pin_settings_merchant_category")
public class PinSettingsMerchantCategory {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 主营商品类别
     */
    @Column(name = "category_name")
    private String categoryName;

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
     * 获取主营商品类别
     *
     * @return category_name - 主营商品类别
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * 设置主营商品类别
     *
     * @param categoryName 主营商品类别
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}