package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.util.base.BaseMapper;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Mapper
public interface PinUserMapper extends BaseMapper<PinUser> {
    PinUser findById(String id);      //通过Email找用户
    Boolean existsById(String id);    //是否存在有这个Id的用户
    void save(PinUser user);    //保存用户信息
    void deleteById(String id);   //通过email删除用户

    PinUser findByEmailA(String email);
}