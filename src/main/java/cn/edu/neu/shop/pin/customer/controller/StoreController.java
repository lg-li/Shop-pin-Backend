package cn.edu.neu.shop.pin.customer.controller;


import cn.edu.neu.shop.pin.customer.service.ProductInfoService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/commons/store")
public class StoreController {

    @Autowired
    private ProductInfoService productInfoService;

    /**
     * 根据店铺Id 获取该店铺的所有在售商品信息
     * @param storeId
     * @return JSONObject
     */
    @GetMapping("/{storeId}/products")
    public JSONObject getProductInfoByStoreId(@PathVariable Integer storeId){
        try{
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, productInfoService.getProInfoByStoreId(storeId));
        }catch(Exception e){
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }
}
