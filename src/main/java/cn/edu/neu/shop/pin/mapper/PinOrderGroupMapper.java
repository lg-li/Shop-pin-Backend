package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.model.PinOrderGroup;
import cn.edu.neu.shop.pin.util.base.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author flyhero
 */

@Component
public interface PinOrderGroupMapper extends BaseMapper<PinOrderGroup> {
    List<PinOrderGroup> getTopTenOrderGroups(Integer storeId);

    List<PinOrderGroup> getAllWithOrderIndividual(Integer storeId);
}