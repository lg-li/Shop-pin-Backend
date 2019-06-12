package cn.edu.neu.shop.pin.websocket;

import java.security.Principal;

public class MerchantPrincipal implements Principal {
    private Integer userId;

    private Integer storeId;

    MerchantPrincipal(Integer userId, Integer storeId) {
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