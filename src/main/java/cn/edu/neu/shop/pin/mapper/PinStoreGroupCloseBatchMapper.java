package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.model.PinStoreGroupCloseBatch;
import cn.edu.neu.shop.pin.util.base.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PinStoreGroupCloseBatchMapper extends BaseMapper<PinStoreGroupCloseBatch> {
    List<PinStoreGroupCloseBatch> getStoreGroupCloseBatchByStoreIdAndTimeDesc(Integer storeId);

    void deleteStoreGroupCloseBatch(Integer storeId, Integer id);
}