package cn.edu.neu.shop.pin.customer.service;

import cn.edu.neu.shop.pin.mapper.PinUserStoreCollectionMapper;
import cn.edu.neu.shop.pin.model.PinUserStoreCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author flyhero
 * 给定用户的userId，查询他的店铺收藏记录
 */

@Service
public class UserStoreCollectionService {

    @Autowired
    private PinUserStoreCollectionMapper pinUserStoreCollectionMapper;

    /**
     * 根据用户id获取其收藏的商品列表及商品信息、店铺信息
     * @param userId
     * @return
     */
    public List<PinUserStoreCollection> getUserStoreCollection(Integer userId) {
        return pinUserStoreCollectionMapper.getUserStoreCollection(userId);
    }
}
