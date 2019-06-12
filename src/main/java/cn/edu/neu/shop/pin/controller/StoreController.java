package cn.edu.neu.shop.pin.controller;


import cn.edu.neu.shop.pin.service.ProductService;
import cn.edu.neu.shop.pin.service.StoreService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commons/store")
public class StoreController {

    private final ProductService productService;

    private final StoreService storeService;

    public StoreController(ProductService productService, StoreService storeService) {
        this.productService = productService;
        this.storeService = storeService;
    }

    /**
     * 根据店铺Id 获取该店铺的所有在售商品信息
     * @param storeId 店铺ID
     * @return JSONObject
     */
    @GetMapping("/{storeId}/products/{pageNum}/{pageSize}")
    public JSONObject getProductInfoByStoreId(@PathVariable(value = "storeId") Integer storeId, @PathVariable(value = "pageNum") Integer pageNum, @PathVariable(value = "pageSize") Integer pageSize) {
        try {
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, productService.getProductByStoreIdByPage(storeId, pageNum, pageSize));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    /**
     * 根据店铺ID 获取该店铺的详细信息
     * @param storeId 店铺ID
     * @return JSONObject
     */
    @GetMapping("/{storeId}")
    public JSONObject getStoreInfoByStoreId(@PathVariable(value = "storeId") Integer storeId) {
        try {
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, storeService.getStoreById(storeId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }
}
