package cn.edu.neu.shop.pin.websocket;

import javax.security.auth.Subject;
import java.security.Principal;

public class MerchantPrinciple implements Principal {
    private Integer userId;

    private Integer storeId;

    public MerchantPrinciple() {
        super();
    }

    public MerchantPrinciple(Integer userId, Integer storeId) {
        this.userId = userId;
        this.storeId = storeId;
    }

    @Override
    public String getName() {
        return toString();
    }

    @Override
    public String toString() {
        String ans = "";
        ans += "merchantId: " + userId.toString() + "\n";
        ans += "storeId: " + storeId.toString() + "\n";
        return ans;
    }

//    @Override
//    public boolean implies(Subject subject) {
//        return false;
//    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }
}