package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.model.PinProduct;
import cn.edu.neu.shop.pin.util.base.BaseMapper;
import io.lettuce.core.dynamic.annotation.Param;

public interface PinProductMapper extends BaseMapper<PinProduct> {

    //根据商品ID获取商品详情
    PinProduct getProductInfoByProductId(@Param("productId") int productId);

    //根据店铺ID获取其在售商品详情
    PinProduct getProductInfoByStoreId(@Param("storeId") int storeId);
}