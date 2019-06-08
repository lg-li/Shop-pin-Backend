package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.model.PinUserAddress;
import cn.edu.neu.shop.pin.util.base.BaseMapper;
import org.springframework.stereotype.Component;

@Component
public interface PinUserAddressMapper extends BaseMapper<PinUserAddress> {
    void setAllDefaultToZero(Integer userId);
}