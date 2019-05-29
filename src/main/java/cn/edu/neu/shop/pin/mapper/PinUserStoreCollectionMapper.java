package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.model.PinUserStoreCollection;
import cn.edu.neu.shop.pin.util.base.BaseMapper;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * @author flyhero
 */

@Component
public interface PinUserStoreCollectionMapper extends BaseMapper<PinUserStoreCollection> {
    List<PinUserStoreCollection> getUserStoreCollection(Integer userId);
}