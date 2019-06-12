package cn.edu.neu.shop.pin.model;

import javax.persistence.*;

@Table(name = "pin_product_attribute_definition")
public class PinProductAttributeDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "product_id")
    private Integer productId;

    /**
     * 属性类别名
     */
    @Column(name = "attribute_name")
    private String attributeName;

    /**
     * 属性类别值（多个用逗号分隔）
     */
    @Column(name = "attribute_values")
    private String attributeValues;

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
     * @return product_id
     */
    public Integer getProductId() {
        return productId;
    }

    /**
     * @param productId
     */
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    /**
     * 获取属性类别名
     *
     * @return attribute_name - 属性类别名
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * 设置属性类别名
     *
     * @param attributeName 属性类别名
     */
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    /**
     * 获取属性类别值（多个用逗号分隔）
     *
     * @return attribute_values - 属性类别值（多个用逗号分隔）
     */
    public String getAttributeValues() {
        return attributeValues;
    }

    /**
     * 设置属性类别值（多个用逗号分隔）
     *
     * @param attributeValues 属性类别值（多个用逗号分隔）
     */
    public void setAttributeValues(String attributeValues) {
        this.attributeValues = attributeValues;
    }

    public PinProductAttributeDefinition() {
    }

    public PinProductAttributeDefinition(Integer productId, String attributeName, String attributeValues) {
        this.productId = productId;
        this.attributeName = attributeName;
        this.attributeValues = attributeValues;
    }
}