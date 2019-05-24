package cn.edu.neu.shop.pin.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "pin_system_admin")
public class PinSystemAdmin {
    /**
     * 后台管理员表ID
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 后台管理员密码
     */
    @Column(name = "password_hash")
    private String passwordHash;

    /**
     * 后台管理员姓名
     */
    private String username;

    /**
     * 后台管理员权限
     */
    private Integer role;

    @Column(name = "last_login_time")
    private Date lastLoginTime;

    @Column(name = "create_time")
    private Date createTime;

    /**
     * 上次登录IP
     */
    @Column(name = "last_login_ip")
    private String lastLoginIp;

    /**
     * 获取后台管理员表ID
     *
     * @return id - 后台管理员表ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置后台管理员表ID
     *
     * @param id 后台管理员表ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取后台管理员密码
     *
     * @return password_hash - 后台管理员密码
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * 设置后台管理员密码
     *
     * @param passwordHash 后台管理员密码
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * 获取后台管理员姓名
     *
     * @return username - 后台管理员姓名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置后台管理员姓名
     *
     * @param username 后台管理员姓名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取后台管理员权限
     *
     * @return role - 后台管理员权限
     */
    public Integer getRole() {
        return role;
    }

    /**
     * 设置后台管理员权限
     *
     * @param role 后台管理员权限
     */
    public void setRole(Integer role) {
        this.role = role;
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
     * 获取上次登录IP
     *
     * @return last_login_ip - 上次登录IP
     */
    public String getLastLoginIp() {
        return lastLoginIp;
    }

    /**
     * 设置上次登录IP
     *
     * @param lastLoginIp 上次登录IP
     */
    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }
}