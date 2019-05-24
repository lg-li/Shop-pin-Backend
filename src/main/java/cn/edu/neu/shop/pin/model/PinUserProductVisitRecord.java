package cn.edu.neu.shop.pin.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "pin_user_product_visit_record")
public class PinUserProductVisitRecord {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "visit_time")
    private Date visitTime;

    @Column(name = "visit_ip")
    private String visitIp;

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
     * @return user_id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
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
     * @return visit_time
     */
    public Date getVisitTime() {
        return visitTime;
    }

    /**
     * @param visitTime
     */
    public void setVisitTime(Date visitTime) {
        this.visitTime = visitTime;
    }

    /**
     * @return visit_ip
     */
    public String getVisitIp() {
        return visitIp;
    }

    /**
     * @param visitIp
     */
    public void setVisitIp(String visitIp) {
        this.visitIp = visitIp;
    }
}