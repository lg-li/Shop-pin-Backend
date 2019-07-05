package cn.edu.neu.shop.pin.controller.commons;

import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.service.ProductService;
import cn.edu.neu.shop.pin.service.security.UserService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author LLG
 * 推荐信息相关 controller
 */
@RestController
@RequestMapping("/commons/recommendation")
public class RecommendationController {

    private final UserService userService;

    private final ProductService productService;

    @Autowired
    public RecommendationController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    /**
     * 获取智能推荐的商品信息，支持分页操作
     *
     * @param pageNum  分页号
     * @param pageSize 分页大小
     * @return 分页的商品规范JSON
     */
    @GetMapping(value = "/product/{pageNum}/{pageSize}")
    public JSONObject getRecommendedProducts(HttpServletRequest httpServletRequest, @PathVariable(value = "pageNum") Integer pageNum, @PathVariable(value = "pageSize") Integer pageSize) {
        try {
            PinUser user = userService.whoAmI(httpServletRequest);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS,
                    PinConstants.ResponseMessage.SUCCESS,
                    productService.getRecommendedProductsByPage(user.getId(), pageNum, pageSize));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }
}
