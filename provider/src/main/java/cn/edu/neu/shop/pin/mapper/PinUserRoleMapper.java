package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.model.PinUserRole;
import cn.edu.neu.shop.pin.util.base.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface PinUserRoleMapper extends BaseMapper<PinUserRole> {
}