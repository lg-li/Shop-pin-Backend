package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.util.base.BaseMapper;
import org.springframework.stereotype.Component;

@Component
public interface PinUserMapper extends BaseMapper<PinUser> {
    PinUser findByEmail(String email);      //通过Email找用户
    Boolean existsByEmail(String email);    //是否存在有这个Email的用户
    void save(PinUser user);    //保存用户信息
    void deleteByEmail(String email);   //通过email删除用户
}