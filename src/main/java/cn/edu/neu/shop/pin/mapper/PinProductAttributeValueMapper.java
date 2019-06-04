package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.model.PinProductAttributeValue;
import cn.edu.neu.shop.pin.util.base.BaseMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public interface PinProductAttributeValueMapper extends BaseMapper<PinProductAttributeValue> {
    void insertProductAttributeValue(Integer productId, String sku, Integer stock,
                                     BigDecimal price, String imageUrl, BigDecimal cost);
}