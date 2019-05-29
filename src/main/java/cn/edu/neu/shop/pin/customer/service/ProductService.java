package cn.edu.neu.shop.pin.customer.service;

import cn.edu.neu.shop.pin.mapper.PinProductMapper;
import cn.edu.neu.shop.pin.model.PinProduct;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * @param productId
     * @return PinProduct类
     */
    public PinProduct getProductByProductId(Integer productId){
        PinProduct pinProduct = pinProductMapper.getProductInfoByProductId(productId);
        return pinProduct;
    }

    /**
     * 根据店铺Id，获取该店铺所有在售商品信息
     * @param storeId
     * @return list
     */
    public List<PinProduct> getProductByStoreId(Integer storeId){
        PinProduct pinProduct = new PinProduct();
        pinProduct.setStoreId(storeId);
        return pinProductMapper.select(pinProduct);
    }

    public PageInfo<PinProduct> getHotProductsByPage(int pageNum, int pageSize) {
        PageHelper.startPage(1, 10);
        List<PinProduct> list = pinProductMapper.getHotProducts();
        System.out.println("Size: " + list.size());
        return new PageInfo<>(list);
//        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(()->pinProductMapper.getHotProducts());
    }
}
