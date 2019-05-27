package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.model.PinProduct;
import cn.edu.neu.shop.pin.util.base.BaseMapper;
import io.lettuce.core.dynamic.annotation.Param;

public interface PinProductMapper extends BaseMapper<PinProduct> {

    //根据商品ID获取商品详情
    PinProduct getProductInfo(@Param("productId") int productId);

}