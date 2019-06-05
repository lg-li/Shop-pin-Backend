package cn.edu.neu.shop.pin.customer.service;

import cn.edu.neu.shop.pin.mapper.PinProductMapper;
import cn.edu.neu.shop.pin.mapper.PinUserProductCollectionMapper;
import cn.edu.neu.shop.pin.model.PinProduct;
import cn.edu.neu.shop.pin.model.PinUserProductCollection;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author flyhero
 * 给定用户的userId，查询他的商品收藏记录
 */
@Service
public class UserProductCollectionService extends AbstractService<PinUserProductCollection> {

    public static final int STATUS_ADD_PRODUCT_SUCCESS = 0;
    public static final int STATUS_ADD_PRODUCT_INVALID_ID = -1;

    @Autowired
    private PinUserProductCollectionMapper pinUserProductCollectionMapper;

    @Autowired
    private PinProductMapper pinProductMapper;

    /**
     * 根据用户id获取其收藏的商品列表及商品信息、店铺信息
     * @param userId
     * @return
     */
    public List<PinUserProductCollection> getUserProductCollection(Integer userId) {
        return pinUserProductCollectionMapper.getUserProductCollection(userId);
    }

    /**
     * 插入一条新的商品收藏
     * @param userId
     * @param productId
     * @return
     */
    @Transactional
    public Integer addProductToCollection(Integer userId, Integer productId) {
        PinProduct pinProduct = pinProductMapper.selectByPrimaryKey(productId);
        if(pinProduct == null) return STATUS_ADD_PRODUCT_INVALID_ID;
        PinUserProductCollection pinUserProductCollection = new PinUserProductCollection();
        pinUserProductCollection.setProductId(productId);
        pinUserProductCollection.setCreateTime(new Date());
        pinUserProductCollection.setUserId(userId);
        this.save(pinUserProductCollection);
        return STATUS_ADD_PRODUCT_SUCCESS;
    }
}
