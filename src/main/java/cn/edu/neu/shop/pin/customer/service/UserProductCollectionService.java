package cn.edu.neu.shop.pin.customer.service;

import cn.edu.neu.shop.pin.mapper.PinUserProductCollectionMapper;
import cn.edu.neu.shop.pin.model.PinUserProductCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author flyhero
 * 给定用户的userId，查询他的商品收藏记录
 */

@Service
public class UserProductCollectionService {

    @Autowired
    private PinUserProductCollectionMapper pinUserProductCollectionMapper;

    /**
     * 根据用户id获取其收藏的商品列表及商品信息、店铺信息
     * @param userId
     * @return
     */
    public List<PinUserProductCollection> getUserProductCollection(Integer userId) {
        return pinUserProductCollectionMapper.getUserProductCollection(userId);
    }
}
