package cn.edu.neu.shop.pin.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "pin_mini_program_form_id_record")
public class PinMiniProgramFormIdRecord {
    /**
     * Log ID
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 微信用户ID
     */
    @Column(name = "wechat_user_id")
    private Integer wechatUserId;

    /**
     * 模板名
     */
    @Column(name = "form_id")
    private String formId;

    /**
     * 添加时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 获取Log ID
     *
     * @return id - Log ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置Log ID
     *
     * @param id Log ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取微信用户ID
     *
     * @return wechat_user_id - 微信用户ID
     */
    public Integer getWechatUserId() {
        return wechatUserId;
    }

    /**
     * 设置微信用户ID
     *
     * @param wechatUserId 微信用户ID
     */
    public void setWechatUserId(Integer wechatUserId) {
        this.wechatUserId = wechatUserId;
    }

    /**
     * 获取模板名
     *
     * @return form_id - 模板名
     */
    public String getFormId() {
        return formId;
    }

    /**
     * 设置模板名
     *
     * @param formId 模板名
     */
    public void setFormId(String formId) {
        this.formId = formId;
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