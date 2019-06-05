package cn.edu.neu.shop.pin.controller;


import cn.edu.neu.shop.pin.service.ProductService;
import cn.edu.neu.shop.pin.service.StoreService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commons/store")
public class StoreController {

    @Autowired
    private ProductService productService;

    @Autowired
    private StoreService storeService;

    /**
     * 根据店铺Id 获取该店铺的所有在售商品信息
     * @param storeId
     * @return JSONObject
     */
    @GetMapping("/{storeId}/products")
    public JSONObject getProductInfoByStoreId(@PathVariable(value = "storeId") Integer storeId){
        try{
            JSONObject data = new JSONObject();
            data.put("list", productService.getProductByStoreId(storeId));
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    /**
     * 根据店铺ID 获取该店铺的详细信息
     * @param storeId
     * @return JSONObject
     */
    @GetMapping("/{storeId}")
    public JSONObject getStoreInfoByStoreId(@PathVariable(value = "storeId") Integer storeId) {
        try{
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, storeService.getStoreById(storeId));
        } catch(Exception e){
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }
}
