package cn.edu.neu.shop.pin.customer.service;

import cn.edu.neu.shop.pin.mapper.PinProductMapper;
import cn.edu.neu.shop.pin.model.PinProduct;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CQF, LYF
 */
@Service
public class ProductService extends AbstractService<PinProduct> {

    @Autowired
    private PinProductMapper pinProductMapper;

    /**
     * 根据商品Id 获取商品详情信息
     * @param productId
     * @return PinProduct类
     */
    public PinProduct getProductByProductId(Integer productId){

        PinProduct pinProduct = pinProductMapper.getProductInfoByProductId(productId);
        return pinProduct;
    }

    /**
     * 根据店铺Id，获取该店铺所有在售商品信息
     * @param storeId
     * @return PinProduct类
     */
    public List<PinProduct> getProductByStoreId(Integer storeId){
        PinProduct pinProduct = new PinProduct();
        pinProduct.setStoreId(storeId);
        return pinProductMapper.select(pinProduct);
    }

    public List<PinProduct> getHotProducts() {
        PinProduct pinProduct = new PinProduct();
        pinProduct.setIsHot(true);
        return mapper.select(pinProduct);
    }
}
