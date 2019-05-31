package cn.edu.neu.shop.pin.customer.service;

import cn.edu.neu.shop.pin.mapper.PinOrderIndividualMapper;
import cn.edu.neu.shop.pin.model.PinOrderIndividual;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ydy
 * 获取is_hot属性为true的商品信息，显示在"热门商品"中
 */

@Service
public class OrderService {
    @Autowired
    PinOrderIndividualMapper individualMapper;

    public PinOrderIndividual createOrderIndividual(){
        return null;
    }
}
