package cn.edu.neu.shop.pin.model;

import javax.persistence.*;

@Table(name = "pin_settings_constant")
public class PinSettingsConstant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "constant_key")
    private String constantKey;

    @Column(name = "constant_value")
    private String constantValue;

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
     * @return constant_key
     */
    public String getConstantKey() {
        return constantKey;
    }

    /**
     * @param constantKey
     */
    public void setConstantKey(String constantKey) {
        this.constantKey = constantKey;
    }

    /**
     * @return constant_value
     */
    public String getConstantValue() {
        return constantValue;
    }

    /**
     * @param constantValue
     */
    public void setConstantValue(String constantValue) {
        this.constantValue = constantValue;
    }
}