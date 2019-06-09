package cn.edu.neu.shop.pin.controller.admin;

import cn.edu.neu.shop.pin.service.ProductCommentService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 后台管理 首页信息
 * 包括 该店铺近七天内的交易数、评论数、浏览量、订单的已支付总数
 */
@RestController
public class AdminHomeController {

    @Autowired
    private ProductCommentService productCommentService;

    /**
     * 显示近七天内的评论数
     */
    @GetMapping()
    public JSONObject getCommentSevenDays(HttpServletRequest httpServletRequest) {
        try{
            int storeId = getCurrentStoreIdFromHeader(httpServletRequest);
            int commentNum[] = productCommentService.getComments(storeId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从请求头中获取当前店铺id
     * @param httpServletRequest
     * @return
     */
    public static int getCurrentStoreIdFromHeader(HttpServletRequest httpServletRequest) {
        String currentStoreId = httpServletRequest.getHeader("Current-Store");
        return Integer.parseInt(currentStoreId);
    }
}
