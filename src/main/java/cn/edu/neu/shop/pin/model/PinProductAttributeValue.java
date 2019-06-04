package cn.edu.neu.shop.pin.model;

import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "pin_product_attribute_value")
public class PinProductAttributeValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 商品id
     */
    @Column(name = "product_id")
    private Integer productId;

    /**
     * 商品属性索引值，分号分割（attribute_value1;attribute_value2;...)
     */
    private String sku;

    /**
     * 库存
     */
    private String stock;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 图片url
     */
    @Column(name = "image_url")
    private String imageUrl;

    /**
     * 成本价
     */
    private BigDecimal cost;

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
     * 获取商品id
     *
     * @return product_id - 商品id
     */
    public Integer getProductId() {
        return productId;
    }

    /**
     * 设置商品id
     *
     * @param productId 商品id
     */
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    /**
     * 获取商品属性索引值，分号分割（attribute_value1;attribute_value2;...)
     *
     * @return sku - 商品属性索引值，分号分割（attribute_value1;attribute_value2;...)
     */
    public String getSku() {
        return sku;
    }

    /**
     * 设置商品属性索引值，分号分割（attribute_value1;attribute_value2;...)
     *
     * @param sku 商品属性索引值，分号分割（attribute_value1;attribute_value2;...)
     */
    public void setSku(String sku) {
        this.sku = sku;
    }

    /**
     * 获取库存
     *
     * @return stock - 库存
     */
    public String getStock() {
        return stock;
    }

    /**
     * 设置库存
     *
     * @param stock 库存
     */
    public void setStock(String stock) {
        this.stock = stock;
    }

    /**
     * 获取价格
     *
     * @return price - 价格
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置价格
     *
     * @param price 价格
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取图片url
     *
     * @return image_url - 图片url
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * 设置图片url
     *
     * @param imageUrl 图片url
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
}