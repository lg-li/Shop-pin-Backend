package cn.edu.neu.shop.pin.customer.controller;

import cn.edu.neu.shop.pin.customer.service.ProductCategoryService;
import cn.edu.neu.shop.pin.customer.service.ProductCommentService;
import cn.edu.neu.shop.pin.customer.service.ProductService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author LLG
 */
@RestController
@RequestMapping("/commons/product")
public class ProductController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCommentService productCommentService;

    /**
     * 层级获取商品分类表
     * @param requestJSON
     * @return JSONObject
     */
    @PostMapping("/category/get-all-by-layer")
    public JSONObject getCategoryByLayer(@RequestBody JSONObject requestJSON) {
        try{
            Integer layer = requestJSON.getInteger("layer");
            JSONObject data = new JSONObject();
            data.put("list", productCategoryService.getProductCategoryByLayer(layer));
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    /**
     * 层级获取商品分类表
     * @return JSONObject
     */
    @GetMapping("/category/all")
    public JSONObject geAllCategory() {
        try{
            JSONObject data = new JSONObject();
            data.put("list", productCategoryService.getProductCategoryAll());
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    /**
     * 层级获取商品分类表
     * @return JSONObject
     */
    @GetMapping("/by-category/{categoryId}/{pageNum}/{pageSize}")
    public JSONObject geProductByCategoryId(@PathVariable(value = "categoryId") Integer categoryId, @PathVariable(value = "pageNum") int pageNum, @PathVariable(value = "pageSize") int pageSize) {
        try{
            JSONObject data = new JSONObject();
            data.put("list", productService.getProductByCategoryIdByPage(categoryId, pageNum, pageSize));
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    /**
     * 通过商品Id 获取商品详情
     * @param productId 商品 ID
     * @return JSONObject
     */
    @GetMapping("/{productId}")
    public JSONObject getProductById(@PathVariable(value = "productId") Integer productId){
        try{
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS,
                    productService.getProductById(productId));
        }catch(Exception e){
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    /**
     * 根据商品Id 获取该商品评论信息
     * @param productId 商品 ID
     * @return JSONObject
     */
    @GetMapping("/{productId}/user-comment")
    public JSONObject getCommentByProductId(@PathVariable(value = "productId") Integer productId){
        try{
            JSONObject data = new JSONObject();
            data.put("list", productCommentService.getCommentByProductId(productId));
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    /**
     * 获取热门商品信息，支持分页操作
     * @param pageNum  分页号
     * @param pageSize 分页大小
     * @return 分页的商品规范JSON
     */
    @GetMapping(value = "/hot/{pageNum}/{pageSize}")
    public JSONObject getHotProducts(@PathVariable(value = "pageNum") int pageNum, @PathVariable(value = "pageSize") int pageSize) {
        try {
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS,
                    PinConstants.ResponseMessage.SUCCESS,
                    productService.getHotProductsByPage(pageNum, pageSize));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    /**
     * 获取全新商品信息，支持分页操作
     * @param pageNum  分页号
     * @param pageSize 分页大小
     * @return 分页的商品规范JSON
     */
    @GetMapping(value = "/new/{pageNum}/{pageSize}")
    public JSONObject getNewProducts(@PathVariable(value = "pageNum") int pageNum, @PathVariable(value = "pageSize") int pageSize) {
        try {
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS,
                    PinConstants.ResponseMessage.SUCCESS,
                    productService.getNewProductsByPage(pageNum, pageSize));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }
}
