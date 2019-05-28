package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.model.PinUserProductCollection;
import cn.edu.neu.shop.pin.util.base.BaseMapper;

import java.util.List;

public interface PinUserProductCollectionMapper extends BaseMapper<PinUserProductCollection> {
    List<PinUserProductCollection> getUserProductCollection();
}