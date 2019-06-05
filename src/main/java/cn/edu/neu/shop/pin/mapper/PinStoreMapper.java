package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.model.PinStore;
import cn.edu.neu.shop.pin.util.base.BaseMapper;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PinStoreMapper extends BaseMapper<PinStore> {
    List<PinStore> selectByOwnerId(Integer ownerId);
}