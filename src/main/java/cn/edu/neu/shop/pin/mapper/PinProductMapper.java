package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.model.PinProduct;
import cn.edu.neu.shop.pin.util.base.BaseMapper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PinProductMapper extends BaseMapper<PinProduct> {

    /**
     * 根据商品ID获取商品详情
     *
     * @param productId
     * @return
     * @author cqf
     */
    PinProduct getProductById(Integer productId);

    /**
     * 筛选所有is_hot属性为true的商品，显示在"热门商品"中
     *
     * @return
     * @author flyhero
     */
    List<PinProduct> getHotProducts();

    /**
     * 筛选所有is_new属性为true的商品，显示在"新品"中
     *
     * @return
     * @author flyhero
     */
    List<PinProduct> getNewProducts();

    //模糊查询
    List<PinProduct> getProductByStoreIdAndKey(@Param("storeId") Integer storeId,@Param("keyWord") String keyWord);

    List<PinProduct> getProductByCategoryId(Integer categoryId);

    List<JSONObject> getProductFromSameStore(Integer storeId);

    List<PinProduct> getProductIsShown(Integer storeId);

    Integer getNumberOfProductLessStock(Integer storeId);

    void updateProductCategory(Integer productId, Integer categoryId);

    List<PinProduct> getIsShownProductInfo(Integer storeId);

    List<PinProduct> getIsReadyProductInfo(Integer storeId);

    List<PinProduct> getIsOutProductInfo(Integer storeId);

    List<PinProduct> getIsAlarmProductInfo(Integer storeId);
}