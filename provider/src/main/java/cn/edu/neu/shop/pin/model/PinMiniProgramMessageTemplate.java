package cn.edu.neu.shop.pin.model;

import javax.persistence.*;

@Table(name = "pin_mini_program_message_template")
public class PinMiniProgramMessageTemplate {
    /**
     * 模板id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 模板编号
     */
    @Column(name = "template_key")
    private String templateKey;

    /**
     * 模板名
     */
    private String name;

    /**
     * 回复内容
     */
    private String content;

    /**
     * 添加时间
     */
    @Column(name = "create_time")
    private String createTime;

    /**
     * 状态
     */
    private Byte status;

    /**
     * 获取模板id
     *
     * @return id - 模板id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置模板id
     *
     * @param id 模板id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取模板编号
     *
     * @return template_key - 模板编号
     */
    public String getTemplateKey() {
        return templateKey;
    }

    /**
     * 设置模板编号
     *
     * @param templateKey 模板编号
     */
    public void setTemplateKey(String templateKey) {
        this.templateKey = templateKey;
    }

    /**
     * 获取模板名
     *
     * @return name - 模板名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置模板名
     *
     * @param name 模板名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取回复内容
     *
     * @return content - 回复内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置回复内容
     *
     * @param content 回复内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取添加时间
     *
     * @return create_time - 添加时间
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * 设置添加时间
     *
     * @param createTime 添加时间
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取状态
     *
     * @return status - 状态
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置状态
     *
     * @param status 状态
     */
    public void setStatus(Byte status) {
        this.status = status;
    }
}