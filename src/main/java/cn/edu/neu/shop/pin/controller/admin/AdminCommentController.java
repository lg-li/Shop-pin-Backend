package cn.edu.neu.shop.pin.controller.admin;

import cn.edu.neu.shop.pin.service.ProductCommentService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class AdminCommentController {

    @Autowired
    private ProductCommentService productCommentService;

    @GetMapping("/comment/goods-comment/{productId}")
    public JSONObject getCommentByProductId(@PathVariable(value = "productId") Integer productId, @PathVariable(value = "pageNum") Integer pageNum, @PathVariable(value = "pageSize") Integer pageSize) {
        try{
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, productCommentService.getCommentAndUserInfoByPage(productId, pageNum, pageSize));
        } catch (Exception e) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    @GetMapping("/goods/goods-with-comment")
    public JSONObject getAllProductWithComment(HttpServletRequest httpServletRequest) {
        try{
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
}
