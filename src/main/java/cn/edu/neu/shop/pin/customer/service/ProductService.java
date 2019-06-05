package cn.edu.neu.shop.pin.customer.service;

import cn.edu.neu.shop.pin.mapper.PinProductAttributeDefinitionMapper;
import cn.edu.neu.shop.pin.mapper.PinProductAttributeValueMapper;
import cn.edu.neu.shop.pin.mapper.PinProductMapper;
import cn.edu.neu.shop.pin.model.PinOrderItem;
import cn.edu.neu.shop.pin.model.PinProduct;
import cn.edu.neu.shop.pin.model.PinProductAttributeDefinition;
import cn.edu.neu.shop.pin.model.PinProductAttributeValue;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LLG, CQF, LYF
 */
@Service
public class ProductService extends AbstractService<PinProduct> {

    @Autowired
    private PinProductMapper pinProductMapper;

    @Autowired
    private PinProductAttributeDefinitionMapper pinProductAttributeDefinitionMapper;

    @Autowired
    private PinProductAttributeValueMapper pinProductAttributeValueMapper;

    /**
     * 根据商品Id 获取商品详情信息
     * @param productId 商品 ID
     * @return 单个PinProduct类实体
     */
    public PinProduct getProductById(Integer productId) {
        PinProduct pinProduct = pinProductMapper.getProductById(productId);
        PinProductAttributeDefinition ppad = new PinProductAttributeDefinition();
        ppad.setProductId(productId);
        List<PinProductAttributeDefinition> defList = pinProductAttributeDefinitionMapper.select(ppad);
        System.out.println("defList.size(): " + defList.size());
        PinProductAttributeValue ppav = new PinProductAttributeValue();
        ppav.setProductId(productId);
        List<PinProductAttributeValue> valList = pinProductAttributeValueMapper.select(ppav);
        System.out.println("valList.size(): " + valList.size());
        pinProduct.setPinProductAttributeDefinitions(defList);
        pinProduct.setPinProductAttributeValues(valList);
        return pinProduct;
    }

    /**
     * 根据店铺Id，获取该店铺所有在售商品信息
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
     * @param categoryId 分类ID
     * @param pageNum
     * @param pageSize
     * @return 商品分页列表
     */
    public PageInfo<PinProduct> getProductByCategoryIdByPage(Integer categoryId, Integer pageNum, Integer pageSize) {
        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(()-> {
            PinProduct pinProduct = new PinProduct();
            pinProduct.setCategoryId(categoryId);
            pinProductMapper.select(pinProduct);
        });
    }

    /**
     * 返回热门商品，支持分页操作
     * @param pageNum 页面编号
     * @param pageSize 页面大小
     * @return 商品分页列表
     */
    public PageInfo<PinProduct> getHotProductsByPage(int pageNum, int pageSize) {
//        PageHelper.startPage(pageNum, pageSize);
//        List<PinProduct> list = pinProductMapper.getHotProducts();
//        return new PageInfo<>(list, pageSize);
        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(()->pinProductMapper.getHotProducts());
    }

    /**
     * 返回最新商品，支持分页操作
     * @param pageNum 页面编号
     * @param pageSize 页面大小
     * @return 商品分页列表
     */
    public PageInfo<PinProduct> getNewProductsByPage(int pageNum, int pageSize) {
//        PageHelper.startPage(pageNum, pageSize);
//        List<PinProduct> list = pinProductMapper.getNewProducts();
//        return new PageInfo<>(list, pageSize);
        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(()->pinProductMapper.getHotProducts());
    }

    /** TODO:ydy未测试
     * 判断传入的 order_item 是否属于同一家店铺
     * @param list 传入的数组，由order_item组成
     * @return  如果都属于同一家店铺，则返回true
     */
    public boolean isBelongSameStore(List<PinOrderItem> list) {
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
}
