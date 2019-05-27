package cn.edu.neu.shop.pin.customer.service;

import cn.edu.neu.shop.pin.mapper.PinProductMapper;
import cn.edu.neu.shop.pin.model.PinProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductInfoService {

    @Autowired
    private PinProductMapper pinProductMapper;

    public PinProduct getProInfo(Integer productId){

        PinProduct pinProduct = pinProductMapper.getProductInfo(productId);
        return pinProduct;
    }
}
