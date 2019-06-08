package cn.edu.neu.shop.pin.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "pin_settings_product_category")
public class PinSettingsProductCategory {
    /**
     * 商品分类表ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 父id
     */
    @Column(name = "parent_category_id")
    private Integer parentCategoryId;

    /**
     * 分类名称
     */
    @Column(name = "category_name")
    private String categoryName;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 图标
     */
    @Column(name = "image_url")
    private String imageUrl;

    /**
     * 是否激活
     */
    @Column(name = "is_activated")
    private Boolean isActivated;

    /**
     * 添加时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 获取商品分类表ID
     *
     * @return id - 商品分类表ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置商品分类表ID
     *
     * @param id 商品分类表ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取父id
     *
     * @return parent_category_id - 父id
     */
    public Integer getParentCategoryId() {
        return parentCategoryId;
    }

    /**
     * 设置父id
     *
     * @param parentCategoryId 父id
     */
    public void setParentCategoryId(Integer parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    /**
     * 获取分类名称
     *
     * @return category_name - 分类名称
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * 设置分类名称
     *
     * @param categoryName 分类名称
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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
     * 获取图标
     *
     * @return image_url - 图标
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * 设置图标
     *
     * @param imageUrl 图标
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * 获取是否激活
     *
     * @return is_activated - 是否激活
     */
    public Boolean getIsActivated() {
        return isActivated;
    }

    /**
     * 设置是否激活
     *
     * @param isActivated 是否激活
     */
    public void setIsActivated(Boolean isActivated) {
        this.isActivated = isActivated;
    }

    /**
     * 获取添加时间
     *
     * @return create_time - 添加时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置添加时间
     *
     * @param createTime 添加时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}