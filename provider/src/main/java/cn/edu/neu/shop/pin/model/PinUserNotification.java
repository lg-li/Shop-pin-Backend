package cn.edu.neu.shop.pin.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "pin_user_notification")
public class PinUserNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 接收消息的用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 消息通知类型（1：系统消息；2：用户通知）
     */
    private Boolean type;

    /**
     * 发送人
     */
    private String sender;

    /**
     * 通知消息的标题信息
     */
    private String title;

    /**
     * 通知消息的内容
     */
    private String content;

    /**
     * 通知消息发送的时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 发送时间
     */
    @Column(name = "send_time")
    private Date sendTime;

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
     * 获取接收消息的用户id
     *
     * @return user_id - 接收消息的用户id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置接收消息的用户id
     *
     * @param userId 接收消息的用户id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取消息通知类型（1：系统消息；2：用户通知）
     *
     * @return type - 消息通知类型（1：系统消息；2：用户通知）
     */
    public Boolean getType() {
        return type;
    }

    /**
     * 设置消息通知类型（1：系统消息；2：用户通知）
     *
     * @param type 消息通知类型（1：系统消息；2：用户通知）
     */
    public void setType(Boolean type) {
        this.type = type;
    }

    /**
     * 获取发送人
     *
     * @return sender - 发送人
     */
    public String getSender() {
        return sender;
    }

    /**
     * 设置发送人
     *
     * @param sender 发送人
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * 获取通知消息的标题信息
     *
     * @return title - 通知消息的标题信息
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置通知消息的标题信息
     *
     * @param title 通知消息的标题信息
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取通知消息的内容
     *
     * @return content - 通知消息的内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置通知消息的内容
     *
     * @param content 通知消息的内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取通知消息发送的时间
     *
     * @return create_time - 通知消息发送的时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置通知消息发送的时间
     *
     * @param createTime 通知消息发送的时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取发送时间
     *
     * @return send_time - 发送时间
     */
    public Date getSendTime() {
        return sendTime;
    }

    /**
     * 设置发送时间
     *
     * @param sendTime 发送时间
     */
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
}