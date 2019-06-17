package cn.edu.neu.shop.pin.controller.admin;

import cn.edu.neu.shop.pin.service.ProductCommentService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;


@RestController
public class AdminCommentController {

    private final ProductCommentService productCommentService;

    public AdminCommentController(ProductCommentService productCommentService) {
        this.productCommentService = productCommentService;
    }


    @PreAuthorize("hasRole('ROLE_MERCHANT')")
    @PostMapping("/comment/goods-comment")
    public JSONObject getCommentByProductId(@RequestBody JSONObject requestJSON) {
        try {
            Integer productId = requestJSON.getInteger("productId");
            Integer pageNum = requestJSON.getInteger("pageNumber");
            Integer pageSize = requestJSON.getInteger("pageSize");
            List comment = productCommentService.getCommentAndUserInfoByPage(productId);
            List list = productCommentService.getCommentsByPageNumAndSize(comment, pageNum, pageSize);
            JSONObject data = new JSONObject();
            data.put("total", comment.size());
            data.put("list", list);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
        } catch (Exception e) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    @PreAuthorize("hasRole('ROLE_MERCHANT')")
    @GetMapping("/goods/goods-with-comment")
    public JSONObject getAllProductWithComment(HttpServletRequest httpServletRequest) {
        try {
            String store = httpServletRequest.getHeader("Current-Store");
            Integer storeId = Integer.parseInt(store);
            List<JSONObject> list = productCommentService.getProductWithComment(storeId);
            JSONObject data = new JSONObject();
            data.put("list", list);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
        } catch (Exception e) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    @PreAuthorize("hasRole('ROLE_MERCHANT')")
    @PostMapping("/comment/reply-comment")
    public JSONObject updateMerchantReplyComment(@RequestBody JSONObject requestJSON) {
        try {
            Integer commentId = requestJSON.getInteger("commentId");
            String commentContent = requestJSON.getString("commentContent");
            Date date = new Date();
            productCommentService.updateMerchantCommentContent(commentId, commentContent, date);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, null);
        } catch (Exception e) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }
}
