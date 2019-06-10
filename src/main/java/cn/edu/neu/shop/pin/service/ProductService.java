package cn.edu.neu.shop.pin.service;

import cn.edu.neu.shop.pin.mapper.*;
import cn.edu.neu.shop.pin.model.*;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LLG, CQF, LYF
 */
@Service
@Component
public class ProductService extends AbstractService<PinProduct> {

    @Autowired
    private PinProductMapper pinProductMapper;

    @Autowired
    private PinProductAttributeDefinitionMapper pinProductAttributeDefinitionMapper;

    @Autowired
    private PinProductAttributeValueMapper pinProductAttributeValueMapper;

    @Autowired
    private PinUserProductCollectionMapper pinUserProductCollectionMapper;

    @Autowired
    private PinSettingsProductCategoryMapper pinSettingsProductCategoryMapper;

    /**
     * 根据商品Id 获取商品详情信息
     *
     * @param productId 商品 ID
     * @return 单个PinProduct类实体
     */
    public PinProduct getProductById(Integer productId) {
        PinProduct pinProduct = pinProductMapper.getProductById(productId);
        PinProductAttributeDefinition ppad = new PinProductAttributeDefinition();
        ppad.setProductId(productId);
        List<PinProductAttributeDefinition> defList = pinProductAttributeDefinitionMapper.select(ppad);
        PinProductAttributeValue ppav = new PinProductAttributeValue();
        ppav.setProductId(productId);
        List<PinProductAttributeValue> valList = pinProductAttributeValueMapper.select(ppav);
        pinProduct.setProductAttributeDefinitions(defList);
        pinProduct.setProductAttributeValues(valList);
        return pinProduct;
    }

    /**
     * 根据店铺Id，获取该店铺所有在售商品信息
     *
     * @param storeId 店铺 ID
     * @return 商品列表
     */
    public List<PinProduct> getProductByStoreId(Integer storeId) {
        PinProduct pinProduct = new PinProduct();
        pinProduct.setStoreId(storeId);
        return pinProductMapper.select(pinProduct);
    }

    /**
     * 根据分类ID，获取该分类下所有在售商品信息
     *
     * @param categoryId 分类ID
     * @param pageNum
     * @param pageSize
     * @return 商品分页列表
     */
    public PageInfo<PinProduct> getProductByCategoryIdByPage(Integer categoryId, Integer pageNum, Integer pageSize) {
        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            PinProduct pinProduct = new PinProduct();
            pinProduct.setCategoryId(categoryId);
            pinProductMapper.select(pinProduct);
            // pinProductMapper.getProductByCategoryId(categoryId);
        });
    }

    /**
     * 返回热门商品，支持分页操作
     *
     * @param pageNum  页面编号
     * @param pageSize 页面大小
     * @return 商品分页列表
     */
    public PageInfo<PinProduct> getHotProductsByPage(int pageNum, int pageSize) {
//        PageHelper.startPage(pageNum, pageSize);
//        List<PinProduct> list = pinProductMapper.getHotProducts();
//        return new PageInfo<>(list, pageSize);
        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> pinProductMapper.getHotProducts());
    }

    /**
     * 返回最新商品，支持分页操作
     *
     * @param pageNum  页面编号
     * @param pageSize 页面大小
     * @return 商品分页列表
     */
    public PageInfo<PinProduct> getNewProductsByPage(int pageNum, int pageSize) {
//        PageHelper.startPage(pageNum, pageSize);
//        List<PinProduct> list = pinProductMapper.getNewProducts();
//        return new PageInfo<>(list, pageSize);
        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> pinProductMapper.getNewProducts());
    }

    /**
     * TODO:ydy未测试
     * 判断传入的 order_item 是否属于同一家店铺
     *
     * @param list 传入的数组，由order_item组成
     * @return 如果都属于同一家店铺，则返回true
     */
    boolean isBelongSameStore(List<PinOrderItem> list) {
        boolean isSameStore = true;
        //判断是否属于一家店铺
        Integer storeId = getProductById(list.get(0).getProductId()).getStoreId();
        for (PinOrderItem pinOrderItem : list) {
            Integer id = pinOrderItem.getProductId();
            if (!storeId.equals(getProductById(id).getStoreId()))
                isSameStore = false;
        }
        return isSameStore;
    }

    /**
     * 根据userId和productId判断某一商品是否被某一用户收藏
     *
     * @param userId
     * @param productId
     * @return
     */
    public boolean isCollected(Integer userId, Integer productId) {
        PinUserProductCollection p = new PinUserProductCollection();
        p.setUserId(userId);
        p.setProductId(productId);
        List<PinUserProductCollection> list = pinUserProductCollectionMapper.select(p);
        return list.size() != 0;
    }

    /**
     * TODO:未测试
     * 管理端，传入一个商品 queryType，返回满足条件的商品的list
     *
     * @param products  传入一系列商品的list
     * @param queryType 传入条件
     * @return 返回满足条件的商品list
     */
    public List<PinProduct> judgeQueryType(List<PinProduct> products, String queryType) {
        List<PinProduct> list = new ArrayList<PinProduct>();
        for (PinProduct item : products) {
            switch (queryType) {
                case "OUT":
                    if (item.getStockCount() == 0)
                        list.add(item);
                    break;
                case "SALING":
                    if (item.getIsShown())
                        list.add(item);
                    break;
                case "READY":
                    if ((!item.getIsShown()) && (item.getStockCount() != 0))
                        list.add(item);
                    break;
                case "ALARM":
                    if (item.getStockCount() <= PinConstants.ProductStatus.ALARM)
                        list.add(item);
                    break;
            }
        }
        return list;

    }

    public JSONObject getProductInfoFromSameStore(Integer storeId) {
        return pinProductMapper.getProductFromSameStore(storeId);
    }

    /**
     * 店铺库存预警商品个数
     * @param storeId
     * @return
     */
    public Integer getProductLessStock(Integer storeId) {
        return pinProductMapper.getNumberOfProductLessStock(storeId);
    }
}
