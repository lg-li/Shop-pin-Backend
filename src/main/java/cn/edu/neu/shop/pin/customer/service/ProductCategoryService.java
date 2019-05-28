package cn.edu.neu.shop.pin.customer.service;


import cn.edu.neu.shop.pin.mapper.PinSettingsProductCategoryMapper;
import cn.edu.neu.shop.pin.model.PinSettingsProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoryService {

    @Autowired
    private PinSettingsProductCategoryMapper pinSettingsProductCategoryMapper;

    /**
     * 层级获取商品分类表
     * @param layer
     * @return List
     */
    public List<PinSettingsProductCategory> getProductCategoryByLayer(Integer layer) {
        if(layer == null || layer.equals(0)) {
            return pinSettingsProductCategoryMapper.getAllParentProductCategory();
        } else {
            return pinSettingsProductCategoryMapper.getAllSubProductCategory();
        }

    }
}
