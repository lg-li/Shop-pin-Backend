package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.model.PinOrderItem;
import cn.edu.neu.shop.pin.util.base.BaseMapper;
import org.springframework.stereotype.Component;

/**
 * @author flyhero
 */

@Component
public interface PinOrderItemMapper extends BaseMapper<PinOrderItem> {

    void addOrderItem(Integer productId, Integer userId, Integer amount);

    PinOrderItem getUnsubmittedOrderItemByUserIdAndProductId(Integer userId, Integer productId);
}