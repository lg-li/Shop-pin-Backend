package cn.edu.neu.shop.pin.service;

import cn.edu.neu.shop.pin.mapper.PinStoreGroupCloseBatchMapper;
import cn.edu.neu.shop.pin.model.PinStoreGroupCloseBatch;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreCloseBatchService extends AbstractService<PinStoreGroupCloseBatch> {

    @Autowired
    private PinStoreGroupCloseBatchMapper pinStoreGroupCloseBatchMapper;

    public List<PinStoreGroupCloseBatch> getGroupCloseBatchTime(Integer storeId) {
        PinStoreGroupCloseBatch pinStoreGroupCloseBatch = new PinStoreGroupCloseBatch();
        pinStoreGroupCloseBatch.setStoreId(storeId);
        return pinStoreGroupCloseBatchMapper.select(pinStoreGroupCloseBatch);
    }
}
