package cn.edu.neu.shop.pin.model;

import org.springframework.security.core.GrantedAuthority;

public enum PinRole implements GrantedAuthority {
    ROLE_USER, ROLE_DEALER, ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
