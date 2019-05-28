package cn.edu.neu.shop.pin.customer.service;

import cn.edu.neu.shop.pin.mapper.PinProductMapper;
import cn.edu.neu.shop.pin.model.PinProduct;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author flyhero
 * 获取is_hot属性为true的商品信息，显示在"热门商品"中
 */

@Service
public class HotProductService extends AbstractService<PinProduct> {

    @Autowired
    private PinProductMapper pinProductMapper;

    public List<PinProduct> getHotProducts() {
        PinProduct pinProduct = new PinProduct();
        pinProduct.setIsHot(true);
        return mapper.select(pinProduct);
    }
}
