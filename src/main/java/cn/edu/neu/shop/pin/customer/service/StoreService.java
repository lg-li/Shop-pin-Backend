package cn.edu.neu.shop.pin.customer.service;

import cn.edu.neu.shop.pin.mapper.PinStoreMapper;
import cn.edu.neu.shop.pin.model.PinStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreService {

    @Autowired
    private PinStoreMapper pinStoreMapper;

    /**
     * 根据店铺ID 获取该店铺的详细信息
     * @param storeId
     * @return PinStore类
     */
    public PinStore getStoreInfo(int storeId){
        return pinStoreMapper.getStoreInfoByStoreId(storeId);
    }
}
