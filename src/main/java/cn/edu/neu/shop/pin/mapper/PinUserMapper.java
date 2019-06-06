package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.util.base.BaseMapper;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public interface PinUserMapper extends BaseMapper<PinUser> {
    PinUser findById(Integer id);    //通过id找用户
    Boolean existsById(Integer id);    //是否存在有这个Id的用户
}