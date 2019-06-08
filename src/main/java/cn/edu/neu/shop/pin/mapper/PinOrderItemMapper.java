package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.model.PinOrderItem;
import cn.edu.neu.shop.pin.util.base.BaseMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author flyhero
 */

@Component
public interface PinOrderItemMapper extends BaseMapper<PinOrderItem> {

    void createOrderItem(Integer userId, Integer productId, Integer skuId, Integer amount);

    void addAmountInExistingOrderItem(Integer amount, BigDecimal totalPrice, BigDecimal totalCost,
                                      Integer userId, Integer skuId);

    PinOrderItem getUnSubmittedOrderItemByUserIdAndProductId(Integer userId, Integer productId, Integer skuId);
}