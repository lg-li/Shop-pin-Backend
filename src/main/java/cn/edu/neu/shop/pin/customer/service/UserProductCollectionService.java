package cn.edu.neu.shop.pin.customer.service;

import cn.edu.neu.shop.pin.mapper.PinUserProductCollectionMapper;
import cn.edu.neu.shop.pin.model.PinUserProductCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProductCollectionService {

    @Autowired
    private PinUserProductCollectionMapper pinUserProductCollectionMapper;

    /**
     *
     * @param userId
     * @return
     */
    public List<PinUserProductCollection> getUserProductCollection(Integer userId) {
        return pinUserProductCollectionMapper.getUserProductCollection(userId);
    }
}
