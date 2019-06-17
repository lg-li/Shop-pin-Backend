package cn.edu.neu.shop.pin.controller.commons;

import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.service.UserProductCollectionService;
import cn.edu.neu.shop.pin.service.UserStoreCollectionService;
import cn.edu.neu.shop.pin.service.security.UserService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户收藏相关操作 controller
 */
@RestController
@RequestMapping("/commons/user")
public class UserCollectionController {

    private final UserService userService;

    private final UserStoreCollectionService userStoreCollectionService;

    private final UserProductCollectionService userProductCollectionService;

    @Autowired
    public UserCollectionController(UserService userService, UserStoreCollectionService userStoreCollectionService, UserProductCollectionService userProductCollectionService) {
        this.userService = userService;
        this.userStoreCollectionService = userStoreCollectionService;
        this.userProductCollectionService = userProductCollectionService;
    }


    /**
     * @param httpServletRequest 请求对象
     * @return 响应 JSON
     * @author flyhero
     * 获取商品收藏product-collection
     */
    @GetMapping("/product-collection")
    public JSONObject getUserProductCollection(HttpServletRequest httpServletRequest) {
        PinUser user = userService.whoAmI(httpServletRequest);
        try {
            JSONObject data = new JSONObject();
            data.put("list", userProductCollectionService.getUserProductCollection(user.getId()));
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    /**
     * @param httpServletRequest 请求对象
     * @return 响应 JSON
     * @author flyhero
     * 获取店铺收藏store-collection
     */
    @GetMapping("/store-collection")
    public JSONObject getUserStoreCollection(HttpServletRequest httpServletRequest) {
        PinUser user = userService.whoAmI(httpServletRequest);
        try {
            JSONObject data = new JSONObject();
            data.put("list", userStoreCollectionService.getUserStoreCollection(user.getId()));
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    /**
     * @param httpServletRequest HttpServlet请求体
     * @param requestJSON        请求体JSON 包含要收藏的商品productId
     * @return 添加收藏状态 JSON
     * @author flyhero
     * 添加商品收藏
     */
    @PostMapping("/product-collection")
    public JSONObject addProductToCollection(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        try {
            PinUser user = userService.whoAmI(httpServletRequest);
            Integer userId = user.getId();
            Integer productId = requestJSON.getInteger("productId");
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS,
                    userProductCollectionService.addProductToCollection(productId, userId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    /**
     * @param httpServletRequest HttpServlet请求体
     * @param productId          收藏的商品ID
     * @return 响应JSON
     * @author flyhero
     * 删除商品收藏
     */
    @DeleteMapping("/product-collection/{productId}")
    public JSONObject deleteUserProductCollection(HttpServletRequest httpServletRequest, @PathVariable Integer productId) {
        PinUser user = userService.whoAmI(httpServletRequest);
        try {
            int code = userProductCollectionService.deleteStoreCollection(user.getId(), productId);
            if (code == UserProductCollectionService.STATUS_DELETE_PRODUCT_SUCCESS) {
                return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, null);
            } else if (code == UserProductCollectionService.STATUS_DELETE_PRODUCT_PERMISSION_DENIED) {
                return ResponseWrapper.wrap(PinConstants.StatusCode.PERMISSION_DENIED, "无权限删除", null);
            } else if (code == UserProductCollectionService.STATUS_ADD_PRODUCT_INVALID_ID) {
                return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, "删除失败", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
        return null;
    }

    /**
     * @param httpServletRequest HttpServlet请求体
     * @param requestJSON        请求体JSON
     * @return 响应JSON
     * @author flyhero
     * 添加店铺收藏
     */
    @PostMapping("/store-collection")
    public JSONObject addStoreToCollection(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        try {
            PinUser user = userService.whoAmI(httpServletRequest);
            Integer userId = user.getId();
            Integer storeId = requestJSON.getInteger("storeId");
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS,
                    userStoreCollectionService.addStoreToCollection(storeId, userId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    /**
     * @param httpServletRequest HttpServlet请求体
     * @param storeId            收藏的店铺ID
     * @return 响应JSON
     * @author flyhero
     * 删除店铺收藏
     */
    @DeleteMapping("/store-collection/{storeId}")
    public JSONObject deleteUserStoreCollection(HttpServletRequest httpServletRequest, @PathVariable Integer storeId) {
        PinUser user = userService.whoAmI(httpServletRequest);
        try {
            int code = userProductCollectionService.deleteStoreCollection(user.getId(), storeId);
            if (code == UserStoreCollectionService.STATUS_DELETE_STORE_SUCCESS) {
                return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, null);
            } else if (code == UserStoreCollectionService.STATUS_DELETE_STORE_PERMISSION_DENIED) {
                return ResponseWrapper.wrap(PinConstants.StatusCode.PERMISSION_DENIED, "无权限删除", null);
            } else if (code == UserStoreCollectionService.STATUS_DELETE_STORE_INVALID_ID) {
                return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, "删除失败", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
        return null;
    }
}
