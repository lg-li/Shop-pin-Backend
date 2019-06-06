package cn.edu.neu.shop.pin.service;

import cn.edu.neu.shop.pin.mapper.PinStoreMapper;
import cn.edu.neu.shop.pin.mapper.PinUserStoreCollectionMapper;
import cn.edu.neu.shop.pin.model.PinStore;
import cn.edu.neu.shop.pin.model.PinUserProductCollection;
import cn.edu.neu.shop.pin.model.PinUserStoreCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author flyhero
 * 给定用户的userId，查询他的店铺收藏记录
 */

@Service
public class UserStoreCollectionService {

    public static final int STATUS_ADD_STORE_SUCCESS = 0;
    public static final int STATUS_ADD_STORE_INVALID_ID = -1;
    public static final int STATUS_DELETE_STORE_SUCCESS = 0;
    public static final int STATUS_DELETE_STORE_INVALID_ID = -1;
    public static final int STATUS_DELETE_STORE_PERMISSION_DENIED = -2;

    @Autowired
    private PinStoreMapper pinStoreMapper;

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

    /**
     * 插入一条新的店铺收藏
     * @param userId
     * @param storeId
     * @return
     */
    @Transactional
    public Integer addStoreToCollection(Integer userId, Integer storeId) {
        PinStore pinStore = pinStoreMapper.selectByPrimaryKey(storeId);
        if(pinStore == null) return STATUS_ADD_STORE_INVALID_ID;
        PinUserStoreCollection pinUserStoreCollection = new PinUserStoreCollection();
        pinUserStoreCollection.setUserId(userId);
        pinUserStoreCollection.setStoreId(storeId);
        pinUserStoreCollection.setCreateTime(new Date());
        return STATUS_ADD_STORE_SUCCESS;
    }

    /**
     * 根据userId和storeId删除一条店铺收藏
     * @param userId
     * @param storeId
     * @return
     */
    @Transactional
    public Integer deleteStoreCollection(Integer userId, Integer storeId) {
        PinStore pinStore = pinStoreMapper.selectByPrimaryKey(storeId);
        if(pinStore == null) return STATUS_DELETE_STORE_INVALID_ID;
        PinUserStoreCollection p = new PinUserStoreCollection();
        p.setUserId(userId);
        p.setStoreId(storeId);
        List<PinUserStoreCollection> list = pinUserStoreCollectionMapper.select(p);
        if(list.size() == 0) return STATUS_DELETE_STORE_INVALID_ID;
        for(PinUserStoreCollection pp : list) {
            if(pp.getUserId() != userId)
                return STATUS_DELETE_STORE_PERMISSION_DENIED;
            if(pp.getStoreId() != storeId)
                return STATUS_DELETE_STORE_INVALID_ID;
            pinUserStoreCollectionMapper.delete(pp);
        }
        return STATUS_DELETE_STORE_SUCCESS;
    }
}
