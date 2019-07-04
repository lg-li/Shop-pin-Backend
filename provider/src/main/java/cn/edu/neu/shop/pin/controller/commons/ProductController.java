package cn.edu.neu.shop.pin.controller.commons;

import cn.edu.neu.shop.pin.mapper.PinUserProductCommentMapper;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.model.PinUserProductComment;
import cn.edu.neu.shop.pin.service.ProductCategoryService;
import cn.edu.neu.shop.pin.service.ProductCommentService;
import cn.edu.neu.shop.pin.service.ProductService;
import cn.edu.neu.shop.pin.service.ProductVisitRecordService;
import cn.edu.neu.shop.pin.service.security.UserService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author LLG
 * 商品基本信息相关 controller
 */
@RestController
@RequestMapping("/commons/product")
public class ProductController {

    private final UserService userService;

    private final ProductService productService;

    private final ProductCommentService productCommentService;

    private final ProductVisitRecordService productVisitRecordService;

    @Autowired
    PinUserProductCommentMapper pinUserProductCommentMapper;
    @Autowired
    public ProductController(UserService userService, ProductService productService, ProductCommentService productCommentService, ProductVisitRecordService productVisitRecordService) {
        this.userService = userService;
        this.productService = productService;
        this.productCommentService = productCommentService;
        this.productVisitRecordService = productVisitRecordService;
    }

    /**
     * 层级获取商品分类表
     *
     * @return JSONObject
     */
    @GetMapping("/by-category/{categoryId}/{pageNum}/{pageSize}")
    public JSONObject geProductByCategoryId(@PathVariable(value = "categoryId") Integer categoryId, @PathVariable(value = "pageNum") Integer pageNum, @PathVariable(value = "pageSize") Integer pageSize) {
        try {
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, productService.getProductByCategoryIdByPage(categoryId, pageNum, pageSize));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    /**
     * 通过商品Id 获取商品详情
     *
     * @param productId 商品 ID
     * @return JSONObject
     */
    @GetMapping("/{productId}")
    public JSONObject getProductById(@PathVariable(value = "productId") Integer productId) {
        try {
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS,
                    productService.getProductByIdWithOneComment(productId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    /**
     * 根据商品Id 获取该商品评论信息，支持分页操作
     *
     * @param productId 商品 ID
     * @return JSONObject
     */
    @GetMapping("/{productId}/comment/{pageNum}/{pageSize}")
    public JSONObject getCommentByProductIdByPage(@PathVariable(value = "productId") Integer productId, @PathVariable(value = "pageNum") Integer pageNum, @PathVariable(value = "pageSize") Integer pageSize) {
        try {
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, productCommentService.getCommentByProductIdByPage(productId, pageNum, pageSize));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    /**
     * 获取热门商品信息，支持分页操作
     *
     * @param pageNum  分页号
     * @param pageSize 分页大小
     * @return 分页的商品规范JSON
     */
    @GetMapping(value = "/hot/{pageNum}/{pageSize}")
    public JSONObject getHotProducts(@PathVariable(value = "pageNum") Integer pageNum, @PathVariable(value = "pageSize") Integer pageSize) {
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
     *
     * @param pageNum  分页号
     * @param pageSize 分页大小
     * @return 分页的商品规范JSON
     */
    @GetMapping(value = "/new/{pageNum}/{pageSize}")
    public JSONObject getNewProducts(@PathVariable(value = "pageNum") Integer pageNum, @PathVariable(value = "pageSize") Integer pageSize) {
        try {
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS,
                    PinConstants.ResponseMessage.SUCCESS,
                    productService.getNewProductsByPage(pageNum, pageSize));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    /**
     * 判断某一商品是否已被收藏
     *
     * @param httpServletRequest HttpServlet请求体
     * @param productId          商品ID
     * @return 响应JSON
     */
    @GetMapping(value = "/{productId}/is-collected")
    public JSONObject isCollected(HttpServletRequest httpServletRequest, @PathVariable(value = "productId") Integer productId) {
        try {
            PinUser user = userService.whoAmI(httpServletRequest);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS,
                    PinConstants.ResponseMessage.SUCCESS,
                    productService.isCollected(user.getId(), productId));
        } catch (Exception e) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    /**
     * 增加商品访问量 && 创建一条商品访问记录
     *
     * @param httpServletRequest HttpServlet请求体
     * @param requestJSON        包含productId和visitTime（Date格式）
     * @return 响应JSON
     */
    @PostMapping("/create-visit-record")
    public JSONObject createVisitRecord(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        PinUser user = userService.whoAmI(httpServletRequest);
        Integer productId = requestJSON.getInteger("productId");
        Date visitTime = requestJSON.getDate("visitTime");
        String visitIp = httpServletRequest.getRemoteAddr();
        try {
            productVisitRecordService.createVisitRecord(user.getId(), productId, visitTime, visitIp);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, null);
        } catch (Exception e) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    @PostMapping("/get-product-average-score")
    public JSONObject returnPraise(@RequestBody JSONObject request){
        try {
            Integer productId = request.getInteger("id");
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, pinUserProductCommentMapper.getAvgScore(productId));
        } catch (Exception e) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }
}
