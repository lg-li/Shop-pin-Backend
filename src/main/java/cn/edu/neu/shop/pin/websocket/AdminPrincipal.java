package cn.edu.neu.shop.pin.websocket;

import java.security.Principal;

public class AdminPrincipal implements Principal {

    private Integer userId;

    public AdminPrincipal() {
        super();
    }

    public AdminPrincipal(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String getName() {
        return toString();
    }

    @Override
    public String toString() {
        String ans = "";
        ans += "adminId: " + userId.toString() + "\n";
        return ans;
    }
}
