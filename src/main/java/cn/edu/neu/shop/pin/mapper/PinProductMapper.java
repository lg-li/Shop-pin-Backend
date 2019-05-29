package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.model.PinProduct;
import cn.edu.neu.shop.pin.util.base.BaseMapper;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public interface PinProductMapper extends BaseMapper<PinProduct> {

    /**
     * 根据商品ID获取商品详情
     * @author cqf
     * @param productId
     * @return
     */
    PinProduct getProductInfoByProductId(@Param("productId") Integer productId);

    /**
     * 筛选所有is_hot属性为true的商品，显示在"热门商品"中
     * @author flyhero
     * @return
     */

    List<PinProduct> getHotProducts();
}