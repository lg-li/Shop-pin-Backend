package cn.edu.neu.shop.pin.model;

import com.baomidou.mybatisplus.annotation.TableField;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Table(name = "pin_product")
public class PinProduct {
    /**
     * 商品id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 商户Id(0为总后台管理员创建,不为0的时候是商户后台创建)
     */
    @Column(name = "store_id")
    private Integer storeId;

    /**
     * 分类id
     */
    @Column(name = "category_id")
    private Integer categoryId;

    /*
     * 分类name (已注释)
     */
//    @TableField(exist = false)
//    private String categoryName;

    /**
     * 商品图片（可以多个）
     */
    @Column(name = "image_urls")
    private String imageUrls;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品简介
     */
    private String info;

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 折扣前原价（前端显示为灰色横线划掉的样式）
     */
    @Column(name = "price_before_discount")
    private BigDecimal priceBeforeDiscount;

    /**
     * 单位名
     */
    @Column(name = "unit_name")
    private String unitName;

    /**
     * 库存
     */
    @Column(name = "stock_count")
    private Integer stockCount;

    /**
     * 销量
     */
    @Column(name = "sold_count")
    private Integer soldCount;

    /**
     * 状态（0：未上架，1：上架）
     */
    @Column(name = "is_shown")
    private Boolean isShown;

    /**
     * 是否热卖
     */
    @Column(name = "is_hot")
    private Boolean isHot;

    /**
     * 是否新品
     */
    @Column(name = "is_new")
    private Boolean isNew;

    /**
     * 邮费
     */
    @Column(name = "shipping_fee")
    private BigDecimal shippingFee;

    /**
     * 是否包邮：0: 否；1：是
     */
    @Column(name = "is_free_shipping")
    private Boolean isFreeShipping;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 购买将获得的积分
     */
@Column(name = "credit_to_give")
    private Integer creditToGive;

    /**
     * 成本价
     */
    private BigDecimal cost;

    /**
     * 浏览量
     */
    @Column(name = "visit_count")
    private Integer visitCount;

    /**
     * 产品描述
     */
    private String description;

    private PinStore store;

    private List<PinProductAttributeDefinition> productAttributeDefinitions;

    private List<PinProductAttributeValue> productAttributeValues;

    /**
     * 获取商品id
     *
     * @return id - 商品id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置商品id
     *
     * @param id 商品id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取商户Id(0为总后台管理员创建,不为0的时候是商户后台创建)
     *
     * @return store_id - 商户Id(0为总后台管理员创建,不为0的时候是商户后台创建)
     */
    public Integer getStoreId() {
        return storeId;
    }

    /**
     * 设置商户Id(0为总后台管理员创建,不为0的时候是商户后台创建)
     *
     * @param storeId 商户Id(0为总后台管理员创建,不为0的时候是商户后台创建)
     */
    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    /**
     * 获取分类id
     *
     * @return category_id - 分类id
     */
    public Integer getCategoryId() {
        return categoryId;
    }

    /**
     * 设置分类id
     *
     * @param categoryId 分类id
     */
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * 获取商品图片（可以多个）
     *
     * @return image_urls - 商品图片（可以多个）
     */
    public String getImageUrls() {
        return imageUrls;
    }

    /**
     * 设置商品图片（可以多个）
     *
     * @param imageUrls 商品图片（可以多个）
     */
    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
    }

    /**
     * 获取商品名称
     *
     * @return name - 商品名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置商品名称
     *
     * @param name 商品名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取商品简介
     *
     * @return info - 商品简介
     */
    public String getInfo() {
        return info;
    }

    /**
     * 设置商品简介
     *
     * @param info 商品简介
     */
    public void setInfo(String info) {
        this.info = info;
    }

    /**
     * 获取关键字
     *
     * @return keyword - 关键字
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * 设置关键字
     *
     * @param keyword 关键字
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * 获取商品价格
     *
     * @return price - 商品价格
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置商品价格
     *
     * @param price 商品价格
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取折扣前原价（前端显示为灰色横线划掉的样式）
     *
     * @return price_before_discount - 折扣前原价（前端显示为灰色横线划掉的样式）
     */
    public BigDecimal getPriceBeforeDiscount() {
        return priceBeforeDiscount;
    }

    /**
     * 设置折扣前原价（前端显示为灰色横线划掉的样式）
     *
     * @param priceBeforeDiscount 折扣前原价（前端显示为灰色横线划掉的样式）
     */
    public void setPriceBeforeDiscount(BigDecimal priceBeforeDiscount) {
        this.priceBeforeDiscount = priceBeforeDiscount;
    }

    /**
     * 获取单位名
     *
     * @return unit_name - 单位名
     */
    public String getUnitName() {
        return unitName;
    }

    /**
     * 设置单位名
     *
     * @param unitName 单位名
     */
    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    /**
     * 获取库存
     *
     * @return stock_count - 库存
     */
    public Integer getStockCount() {
        return stockCount;
    }

    /**
     * 设置库存
     *
     * @param stockCount 库存
     */
    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    /**
     * 获取销量
     *
     * @return sold_count - 销量
     */
    public Integer getSoldCount() {
        return soldCount;
    }

    /**
     * 设置销量
     *
     * @param soldCount 销量
     */
    public void setSoldCount(Integer soldCount) {
        this.soldCount = soldCount;
    }

    /**
     * 获取状态（0：未上架，1：上架）
     *
     * @return is_shown - 状态（0：未上架，1：上架）
     */
    public Boolean getIsShown() {
        return isShown;
    }

    /**
     * 设置状态（0：未上架，1：上架）
     *
     * @param isShown 状态（0：未上架，1：上架）
     */
    public void setIsShown(Boolean isShown) {
        this.isShown = isShown;
    }

    /**
     * 获取是否热卖
     *
     * @return is_hot - 是否热卖
     */
    public Boolean getIsHot() {
        return isHot;
    }

    /**
     * 设置是否热卖
     *
     * @param isHot 是否热卖
     */
    public void setIsHot(Boolean isHot) {
        this.isHot = isHot;
    }

    /**
     * 获取是否新品
     *
     * @return is_new - 是否新品
     */
    public Boolean getIsNew() {
        return isNew;
    }

    /**
     * 设置是否新品
     *
     * @param isNew 是否新品
     */
    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    /**
     * 获取邮费
     *
     * @return shipping_fee - 邮费
     */
    public BigDecimal getShippingFee() {
        return shippingFee;
    }

    /**
     * 设置邮费
     *
     * @param shippingFee 邮费
     */
    public void setShippingFee(BigDecimal shippingFee) {
        this.shippingFee = shippingFee;
    }

    /**
     * 获取是否包邮：0: 否；1：是
     *
     * @return is_free_shipping - 是否包邮：0: 否；1：是
     */
    public Boolean getIsFreeShipping() {
        return isFreeShipping;
    }

    /**
     * 设置是否包邮：0: 否；1：是
     *
     * @param isFreeShipping 是否包邮：0: 否；1：是
     */
    public void setIsFreeShipping(Boolean isFreeShipping) {
        this.isFreeShipping = isFreeShipping;
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

    /**
     * 获取购买将获得的积分
     *
     * @return credit_to_give - 购买将获得的积分
     */
    public Integer getCreditToGive() {
        return creditToGive;
    }

    /**
     * 设置购买将获得的积分
     *
     * @param creditToGive 购买将获得的积分
     */
    public void setCreditToGive(Integer creditToGive) {
        this.creditToGive = creditToGive;
    }

    /**
     * 获取成本价
     *
     * @return cost - 成本价
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * 设置成本价
     *
     * @param cost 成本价
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * 获取浏览量
     *
     * @return visit_count - 浏览量
     */
    public Integer getVisitCount() {
        return visitCount;
    }

    /**
     * 设置浏览量
     *
     * @param visitCount 浏览量
     */
    public void setVisitCount(Integer visitCount) {
        this.visitCount = visitCount;
    }

    /**
     * 获取产品描述
     *
     * @return description - 产品描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置产品描述
     *
     * @param description 产品描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

//    public String getCategoryName() {
//        return categoryName;
//    }
//
//    public void setCategoryName(String categoryName) {
//        this.categoryName = categoryName;
//    }

    public PinStore getStore() {
        return store;
    }

    public void setStore(PinStore store) {
        this.store = store;
    }

    public List<PinProductAttributeDefinition> getProductAttributeDefinitions() {
        return productAttributeDefinitions;
    }

    public void setProductAttributeDefinitions(List<PinProductAttributeDefinition> productAttributeDefinitions) {
        this.productAttributeDefinitions = productAttributeDefinitions;
    }

    public List<PinProductAttributeValue> getProductAttributeValues() {
        return productAttributeValues;
    }

    public void setProductAttributeValues(List<PinProductAttributeValue> productAttributeValues) {
        this.productAttributeValues = productAttributeValues;
    }
}