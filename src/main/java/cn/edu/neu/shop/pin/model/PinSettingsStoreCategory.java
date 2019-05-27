package cn.edu.neu.shop.pin.model;

import javax.persistence.*;

@Table(name = "pin_settings_store_category")
public class PinSettingsStoreCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 主营商品类别
     */
    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "image_url")
    private String imageUrl;

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

    /**
     * @return image_url
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * @param imageUrl
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}