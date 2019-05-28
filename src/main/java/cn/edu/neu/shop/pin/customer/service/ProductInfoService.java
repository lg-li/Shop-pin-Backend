package cn.edu.neu.shop.pin.customer.service;

import cn.edu.neu.shop.pin.mapper.PinProductMapper;
import cn.edu.neu.shop.pin.model.PinProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductInfoService {

    @Autowired
    private PinProductMapper pinProductMapper;

    /**
     * 根据商品Id 获取商品详情信息
     * @param productId
     * @return
     */
    public PinProduct getProInfoByProId(Integer productId){

        PinProduct pinProduct = pinProductMapper.getProductInfoByProductId(productId);
        return pinProduct;
    }

    /**
     * 根据店铺Id，获取该店铺所有在售商品信息
     * @param storeId
     * @return
     */
    public PinProduct getProInfoByStoreId(Integer storeId){

        PinProduct pinProduct = pinProductMapper.getProductInfoByStoreId(storeId);
        return pinProduct;
    }
}
