package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.model.PinStore;
import cn.edu.neu.shop.pin.util.base.BaseMapper;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Component;

@Component
public interface PinStoreMapper extends BaseMapper<PinStore> {

}