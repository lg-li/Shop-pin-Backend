package cn.edu.neu.shop.pin.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "pin_user_product_comment")
public class PinUserProductComment {
    /**
     * 评论ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 订单ID
     */
    @Column(name = "order_individual_id")
    private Integer orderIndividualId;

    /**
     * 产品ID
     */
    @Column(name = "product_id")
    private Integer productId;

    /**
     * 0: 好评；1：中评；2：差评
     */
    private Integer grade;

    /**
     * 商品打分
     */
    @Column(name = "product_score")
    private Boolean productScore;

    /**
     * 服务打分
     */
    @Column(name = "service_score")
    private Boolean serviceScore;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 商家回复内容
     */
    @Column(name = "merchant_comment_content")
    private String merchantCommentContent;

    /**
     * 商家回复时间
     */
    @Column(name = "merchant_comment_time")
    private Date merchantCommentTime;

    /**
     * 0未删除；1已删除
     */
    @Column(name = "is_deleted")
    private Boolean isDeleted;

    /**
     * 评论图片
     */
    @Column(name = "images_urls")
    private String imagesUrls;

    /**
     * 获取评论IDID
     *
     * @return id - 评论ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置评论ID
     *
     * @param id 评论ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取用户ID
     *
     * @return user_id - 用户ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户ID
     *
     * @param userId 用户ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取订单ID
     *
     * @return order_individual_id - 订单ID
     */
    public Integer getOrderIndividualId() {
        return orderIndividualId;
    }

    /**
     * 设置订单ID
     *
     * @param orderIndividualId 订单ID
     */
    public void setOrderIndividualId(Integer orderIndividualId) {
        this.orderIndividualId = orderIndividualId;
    }

    /**
     * 获取产品ID
     *
     * @return product_id - 产品ID
     */
    public Integer getProductId() {
        return productId;
    }

    /**
     * 设置产品ID
     *
     * @param productId 产品ID
     */
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    /**
     * 获取0: 好评；1：中评；2：差评
     *
     * @return grade - 0: 好评；1：中评；2：差评
     */
    public Integer getGrade() {
        return grade;
    }

    /**
     * 设置0: 好评；1：中评；2：差评
     *
     * @param grade 0: 好评；1：中评；2：差评
     */
    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    /**
     * 获取商品打分
     *
     * @return product_score - 商品打分
     */
    public Boolean getProductScore() {
        return productScore;
    }

    /**
     * 设置商品打分
     *
     * @param productScore 商品打分
     */
    public void setProductScore(Boolean productScore) {
        this.productScore = productScore;
    }

    /**
     * 获取服务打分
     *
     * @return service_score - 服务打分
     */
    public Boolean getServiceScore() {
        return serviceScore;
    }

    /**
     * 设置服务打分
     *
     * @param serviceScore 服务打分
     */
    public void setServiceScore(Boolean serviceScore) {
        this.serviceScore = serviceScore;
    }

    /**
     * 获取评论内容
     *
     * @return content - 评论内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置评论内容
     *
     * @param content 评论内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取评论时间
     *
     * @return create_time - 评论时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置评论时间
     *
     * @param createTime 评论时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取商家回复内容
     *
     * @return merchant_comment_content - 商家回复内容
     */
    public String getMerchantCommentContent() {
        return merchantCommentContent;
    }

    /**
     * 设置商家回复内容
     *
     * @param merchantCommentContent 商家回复内容
     */
    public void setMerchantCommentContent(String merchantCommentContent) {
        this.merchantCommentContent = merchantCommentContent;
    }

    /**
     * 获取商家回复时间
     *
     * @return merchant_comment_time - 商家回复时间
     */
    public Date getMerchantCommentTime() {
        return merchantCommentTime;
    }

    /**
     * 设置商家回复时间
     *
     * @param merchantCommentTime 商家回复时间
     */
    public void setMerchantCommentTime(Date merchantCommentTime) {
        this.merchantCommentTime = merchantCommentTime;
    }

    /**
     * 获取0未删除；1已删除
     *
     * @return is_deleted - 0未删除；1已删除
     */
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    /**
     * 设置0未删除；1已删除
     *
     * @param isDeleted 0未删除；1已删除
     */
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * 获取评论图片
     *
     * @return images_urls - 评论图片
     */
    public String getImagesUrls() {
        return imagesUrls;
    }

    /**
     * 设置评论图片
     *
     * @param imagesUrls 评论图片
     */
    public void setImagesUrls(String imagesUrls) {
        this.imagesUrls = imagesUrls;
    }
}