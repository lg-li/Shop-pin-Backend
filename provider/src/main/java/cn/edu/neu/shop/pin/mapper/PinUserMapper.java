package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.util.base.BaseMapper;
import org.springframework.stereotype.Component;

@Component
public interface PinUserMapper extends BaseMapper<PinUser> {

    //通过id找用户
    PinUser findById(Integer id);

    //是否存在有这个Id的用户
    Boolean existsById(Integer id);

    // 更新用户积分
    void updateUserCredit(Integer userId, Integer credit);
}