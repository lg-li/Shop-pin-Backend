package cn.edu.neu.shop.pin.customer.service;

import cn.edu.neu.shop.pin.mapper.PinProductMapper;
import cn.edu.neu.shop.pin.model.PinOrderItem;
import cn.edu.neu.shop.pin.model.PinProduct;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * CQF, LYF
 */
@Service
public class ProductService extends AbstractService<PinProduct> {

    @Autowired
    private PinProductMapper pinProductMapper;

    /**
     * 根据商品Id 获取商品详情信息
     *
     * @param productId
     * @return PinProduct类
     */
    public PinProduct getProductByProductId(Integer productId) {
        PinProduct pinProduct = pinProductMapper.getProductInfoByProductId(productId);
        return pinProduct;
    }

    /**
     * 根据店铺Id，获取该店铺所有在售商品信息
     *
     * @param storeId
     * @return list
     */
    public List<PinProduct> getProductByStoreId(Integer storeId) {
        PinProduct pinProduct = new PinProduct();
        pinProduct.setStoreId(storeId);
        return pinProductMapper.select(pinProduct);
    }

    /**
     * TODO 分页插件还没用上
     * 返回热门商品，支持分页操作
     * @param pageNum 页面编号
     * @param pageSize 页面大小
     * @return
     */
    public PageInfo<PinProduct> getHotProductsByPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<PinProduct> list = pinProductMapper.getHotProducts();
        return new PageInfo<>(list, pageSize);
//        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(()->pinProductMapper.getHotProducts());
    }

    /**
     * TODO 分页插件还没用上
     * 返回最新商品，支持分页操作
     * @param pageNum 页面编号
     * @param pageSize 页面大小
     * @return
     */
    public PageInfo<PinProduct> getNewProductsByPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<PinProduct> list = pinProductMapper.getNewProducts();
        return new PageInfo<>(list, pageSize);
//        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(()->pinProductMapper.getHotProducts());
    }

    /** TODO:ydy未测试
     * 判断传入的 order_item 是否属于同一家店铺
     *
     * @param list 传入的数组，由order_item组成
     * @return  如果都属于同一家店铺，则返回true
     */
    public boolean isBelongSameStore(ArrayList<PinOrderItem> list) {
        boolean isSameStore = true;
        //判断是否属于一家店铺
        Integer storeId = getProductByProductId(list.get(0).getProductId()).getStoreId();
        for (PinOrderItem pinOrderItem : list) {
            Integer id = pinOrderItem.getProductId();
            if (!storeId.equals(getProductByProductId(id).getStoreId()))
                isSameStore = false;
        }
        return isSameStore;
    }
}
