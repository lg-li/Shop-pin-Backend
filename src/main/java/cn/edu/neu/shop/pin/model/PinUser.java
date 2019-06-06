package cn.edu.neu.shop.pin.model;

import io.swagger.models.auth.In;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Table(name = "pin_user")
public class PinUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 电话号
     */
    private String phone;

    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "last_login_time")
    private Date lastLoginTime;

    @Column(name = "last_password_edit_time")
    private Date lastPaswordEditTime;

    private List<PinRole> roles;

    /**
     * 头像链接（若微信有则复制微信的数据）
     */
    @Column(name = "avatar_url")
    private String avatarUrl;

    private String nickname;

    private BigDecimal balance;

    /**
     * 用户积分(通过签到和购买获得)
     */
    private Integer credit;

    @Column(name = "last_login_ip")
    private String lastLoginIp;

    @Column(name = "create_ip")
    private String createIp;

    /**
     * 1: 男；2：女；0:未知
     */
    private Integer gender;

    public PinUser(String phone, String email, String password, Timestamp createTime, Timestamp lastLoginTime, Timestamp lastPaswordEditTime, String avatarUrl, String nickname, BigDecimal balance, Integer credit, String createIp, String lastLoginIp, Integer gender) {
        this.phone = phone;
        this.email = email;
        this.passwordHash = password;
        this.createTime = createTime;
        this.lastLoginTime = lastLoginTime;
        this.lastPaswordEditTime = lastPaswordEditTime;
        this.avatarUrl = avatarUrl;
        this.nickname = nickname;
        this.balance = balance;
        this.credit = credit;
        this.createIp = createIp;
        this.lastLoginIp = lastLoginIp;
        this.gender = gender;
    }

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
     * 获取电话号
     *
     * @return phone - 电话号
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置电话号
     *
     * @param phone 电话号
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return password_hash
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * @param passwordHash
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return last_login_time
     */
    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * @param lastLoginTime
     */
    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    /**
     * @return last_password_edit_time
     */
    public Date getLastPaswordEditTime() {
        return lastPaswordEditTime;
    }

    /**
     * @param lastPaswordEditTime
     */
    public void setLastPaswordEditTime(Date lastPaswordEditTime) {
        this.lastPaswordEditTime = lastPaswordEditTime;
    }

    /**
     * 获取头像链接（若微信有则复制微信的数据）
     *
     * @return avatar_url - 头像链接（若微信有则复制微信的数据）
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * 设置头像链接（若微信有则复制微信的数据）
     *
     * @param avatarUrl 头像链接（若微信有则复制微信的数据）
     */
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    /**
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return balance
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * @param balance
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * 获取用户积分(通过签到和购买获得)
     *
     * @return credit - 用户积分(通过签到和购买获得)
     */
    public Integer getCredit() {
        return credit;
    }

    /**
     * 设置用户积分(通过签到和购买获得)
     *
     * @param credit 用户积分(通过签到和购买获得)
     */
    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    /**
     * @return last_login_ip
     */
    public String getLastLoginIp() {
        return lastLoginIp;
    }

    /**
     * @param lastLoginIp
     */
    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    /**
     * @return create_ip
     */
    public String getCreateIp() {
        return createIp;
    }

    /**
     * @param createIp
     */
    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    /**
     * 获取1: 男；2：女；0:未知
     *
     * @return gender - 1: 男；2：女；0:未知
     */
    public Integer getGender() {
        return gender;
    }

    /**
     * 设置1: 男；2：女；0:未知
     *
     * @param gender 1: 男；2：女；0:未知
     */
    public void setGender(int gender) {
        this.gender = gender;
    }

    public List<PinRole> getRoles() {
        return roles;
    }

    public void setRoles(List<PinRole> roles) {
        this.roles = roles;
    }

    public PinUser() {
    }

    public PinUser(String phone, String email, String passwordHash, Date createTime, Date lastLoginTime, Date lastPaswordEditTime, String avatarUrl, String nickname, BigDecimal balance, Integer credit, String lastLoginIp, String createIp, Integer gender) {
        this.phone = phone;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createTime = createTime;
        this.lastLoginTime = lastLoginTime;
        this.lastPaswordEditTime = lastPaswordEditTime;
        this.avatarUrl = avatarUrl;
        this.nickname = nickname;
        this.balance = balance;
        this.credit = credit;
        this.lastLoginIp = lastLoginIp;
        this.createIp = createIp;
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "PinUser{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", createTime=" + createTime +
                ", lastLoginTime=" + lastLoginTime +
                ", lastPaswordEditTime=" + lastPaswordEditTime +
                ", roles=" + roles +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", nickname='" + nickname + '\'' +
                ", balance=" + balance +
                ", credit=" + credit +
                ", lastLoginIp='" + lastLoginIp + '\'' +
                ", createIp='" + createIp + '\'' +
                ", gender=" + gender +
                '}';
    }

    public static List<PinRole> transfer(List<PinRole> list){
        ArrayList<PinRole> roleList = new ArrayList<>();
        for (Object integer : list) {
            roleList.add(PinRole.values()[(Integer) integer]);
        }
        return roleList;
    }
}