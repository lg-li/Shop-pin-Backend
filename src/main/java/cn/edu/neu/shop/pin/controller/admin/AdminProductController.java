package cn.edu.neu.shop.pin.controller.admin;

import cn.edu.neu.shop.pin.mapper.PinProductMapper;
import cn.edu.neu.shop.pin.model.PinProduct;
import cn.edu.neu.shop.pin.service.OrderIndividualService;
import cn.edu.neu.shop.pin.service.ProductCategoryService;
import cn.edu.neu.shop.pin.service.ProductService;
import cn.edu.neu.shop.pin.service.security.UserService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/goods")
public class AdminProductController {
    @Autowired
    UserService userService;
    @Autowired
    PinProductMapper productMapper;
    @Autowired
    ProductService productService;
    @Autowired
    ProductCategoryService productCategoryService;
    @Autowired
    OrderIndividualService orderIndividualService;

    /**
     * 返回不同类型商品列表
     * @param req
     * @param requestObject
     * @return
     */
    @PostMapping("/goods-list")
    public JSONObject getProducts(HttpServletRequest req, @RequestBody JSONObject requestObject) {
        try{
            Integer storeId = Integer.valueOf(req.getHeader("Current-Store"));
            String queryType = requestObject.getString("queryType");
            JSONObject data = new JSONObject();
            switch (queryType){
                case "SALING":
                    List<JSONObject> saling = productService.getIsShownProductInfo(storeId);
                    data.put("goodsList", saling);
                    return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
                case "READY":
                    List<JSONObject> ready = productService.getIsReadyProductInfo(storeId);
                    data.put("goodsList", ready);
                    return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
                case "OUT":
                    List<JSONObject> out = productService.getIsOutProductInfo(storeId);
                    data.put("goodsList", out);
                    return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
                case "AlARM":
                    List<JSONObject> alarm = productService.getIsAlarmProductInfo(storeId);
                    data.put("goodsList", alarm);
                    return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
                default:
                    return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, "获取失败", null);
            }
        } catch (Exception e) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, e.getMessage(), null);
        }
    }

    /**
     * 商铺所有者管理本店铺的商品
     * 商品分类部分 获取父级、子级分类名及一些商品信息
     *
     * @param req
     * @return
     */
    @GetMapping("/goods-category")
    public JSONObject getProductFromSameStore(HttpServletRequest req) {
        try {
            String currentStoreId = req.getHeader("Current-Store");
            List<JSONObject> list = productService.getProductInfoFromSameStore(Integer.parseInt(currentStoreId));
            JSONObject data = new JSONObject();
            data.put("list", list);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    @GetMapping("/category-list")
    public JSONObject getProductCatrgory() {
        try {
            JSONArray array = productCategoryService.getProductCategory();
            JSONObject categoryList = new JSONObject();
            categoryList.put("categoryList", array);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, categoryList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    @PutMapping("/update-category")
    public JSONObject updateProductCategory(@RequestBody JSONObject requestJSON) {
        try {
            Integer productId = requestJSON.getInteger("productId");
            Integer categoryId = requestJSON.getInteger("categoryId");
            productService.updateProductCategory(productId, categoryId);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, null);
        } catch (Exception e) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

}
