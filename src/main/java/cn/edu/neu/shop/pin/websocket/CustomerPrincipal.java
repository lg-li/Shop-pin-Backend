package cn.edu.neu.shop.pin.websocket;

import javax.security.auth.Subject;
import java.security.Principal;

public class CustomerPrincipal implements Principal {

    private Integer userId;

    private Integer orderIndividualId;

    private Integer orderGroupId;

    public CustomerPrincipal() {
        super();
    }

    public CustomerPrincipal(Integer userId, Integer orderIndividualId, Integer orderGroupId) {
        this.userId = userId;
        this.orderIndividualId = orderIndividualId;
        this.orderGroupId = orderGroupId;
    }

    @Override
    public String getName() {
        return toString();
    }

    @Override
    public String toString() {
        String ans = "";
        ans += "customerId: " + userId.toString() + "\n";
        ans += "orderIndividualId: " + orderIndividualId.toString() + "\n";
        ans += "orderGroupId: " + orderGroupId.toString() + "\n";
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

    public Integer getOrderIndividualId() {
        return orderIndividualId;
    }

    public void setOrderIndividualId(Integer orderIndividualId) {
        this.orderIndividualId = orderIndividualId;
    }

    public Integer getOrderGroupId() {
        return orderGroupId;
    }

    public void setOrderGroupId(Integer orderGroupId) {
        this.orderGroupId = orderGroupId;
    }
}
