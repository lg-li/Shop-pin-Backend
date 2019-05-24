package cn.edu.neu.shop.pin.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "pin_wechat_user")
public class PinWechatUser {
    /**
     * 微信用户id
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 映射的用户ID
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段
     */
    @Column(name = "union_id")
    private String unionId;

    /**
     * 用户的标识 对当前公众号唯一
     */
    @Column(name = "open_id")
    private String openId;

    /**
     * 用户的昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    @Column(name = "avatar_url")
    private String avatarUrl;

    /**
     * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
     */
    private Boolean gender;

    /**
     * 用户所在城市
     */
    private String city;

    /**
     * 用户的语言，简体中文为zh_CN
     */
    private String language;

    /**
     * 用户所在国家
     */
    private String province;

    /**
     * 用户所在省份
     */
    private String country;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 小程序用户会话密匙
     */
    @Column(name = "session_key")
    private String sessionKey;

    /**
     * 获取微信用户id
     *
     * @return id - 微信用户id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置微信用户id
     *
     * @param id 微信用户id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取映射的用户ID
     *
     * @return user_id - 映射的用户ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置映射的用户ID
     *
     * @param userId 映射的用户ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段
     *
     * @return union_id - 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段
     */
    public String getUnionId() {
        return unionId;
    }

    /**
     * 设置只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段
     *
     * @param unionId 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段
     */
    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    /**
     * 获取用户的标识 对当前公众号唯一
     *
     * @return open_id - 用户的标识 对当前公众号唯一
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * 设置用户的标识 对当前公众号唯一
     *
     * @param openId 用户的标识 对当前公众号唯一
     */
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    /**
     * 获取用户的昵称
     *
     * @return nickname - 用户的昵称
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 设置用户的昵称
     *
     * @param nickname 用户的昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 获取用户头像
     *
     * @return avatar_url - 用户头像
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * 设置用户头像
     *
     * @param avatarUrl 用户头像
     */
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    /**
     * 获取用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
     *
     * @return gender - 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
     */
    public Boolean getGender() {
        return gender;
    }

    /**
     * 设置用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
     *
     * @param gender 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
     */
    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    /**
     * 获取用户所在城市
     *
     * @return city - 用户所在城市
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置用户所在城市
     *
     * @param city 用户所在城市
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 获取用户的语言，简体中文为zh_CN
     *
     * @return language - 用户的语言，简体中文为zh_CN
     */
    public String getLanguage() {
        return language;
    }

    /**
     * 设置用户的语言，简体中文为zh_CN
     *
     * @param language 用户的语言，简体中文为zh_CN
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * 获取用户所在国家
     *
     * @return province - 用户所在国家
     */
    public String getProvince() {
        return province;
    }

    /**
     * 设置用户所在国家
     *
     * @param province 用户所在国家
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * 获取用户所在省份
     *
     * @return country - 用户所在省份
     */
    public String getCountry() {
        return country;
    }

    /**
     * 设置用户所在省份
     *
     * @param country 用户所在省份
     */
    public void setCountry(String country) {
        this.country = country;
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
     * 获取小程序用户会话密匙
     *
     * @return session_key - 小程序用户会话密匙
     */
    public String getSessionKey() {
        return sessionKey;
    }

    /**
     * 设置小程序用户会话密匙
     *
     * @param sessionKey 小程序用户会话密匙
     */
    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }
}