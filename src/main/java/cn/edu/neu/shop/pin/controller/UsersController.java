package cn.edu.neu.shop.pin.controller;

import cn.edu.neu.shop.pin.exception.CheckInFailedException;
import cn.edu.neu.shop.pin.exception.CommentFailedException;
import cn.edu.neu.shop.pin.exception.PermissionDeniedException;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.model.PinUserAddress;
import cn.edu.neu.shop.pin.model.PinUserProductComment;
import cn.edu.neu.shop.pin.service.*;
import cn.edu.neu.shop.pin.service.security.UserService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/commons/user")
public class UsersController {

    private final UserService userService;

    private final AddressService addressService;

    private final UserProductRecordService userProductRecordService;

    private final UserStoreCollectionService userStoreCollectionService;

    private final UserProductCollectionService userProductCollectionService;

    private final ProductCommentService productCommentService;

    private final UserCreditRecordService userCreditRecordService;

    private final PasswordEncoder passwordEncoder;

    public UsersController(UserService userService, AddressService addressService, UserProductRecordService userProductRecordService, UserStoreCollectionService userStoreCollectionService, UserProductCollectionService userProductCollectionService, ProductCommentService productCommentService, UserCreditRecordService userCreditRecordService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.addressService = addressService;
        this.userProductRecordService = userProductRecordService;
        this.userStoreCollectionService = userStoreCollectionService;
        this.userProductCollectionService = userProductCollectionService;
        this.productCommentService = productCommentService;
        this.userCreditRecordService = userCreditRecordService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 获取用户信息
     * @param httpServletRequest HttpServlet请求体
     * @return 包含用户信息以及结果状态的JSONObject
     */
    @GetMapping("/info")
    public JSONObject getUserInfo(HttpServletRequest httpServletRequest) {
        try {
            PinUser user = userService.whoAmI(httpServletRequest);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS,
                    userService.getUserInfoByUserId(user.getId()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    /**
     * @param httpServletRequest HttpServlet请求体
     * @return 包含地址以及结果状态的JSONObject
     * @author flyhero
     * 获取某用户的默认地址
     */
    @GetMapping("/default-address")
    public JSONObject getDefaultAddress(HttpServletRequest httpServletRequest) {
        PinUser user = userService.whoAmI(httpServletRequest);
        try {
            JSONObject data = new JSONObject();
            data.put("defaultAddress", addressService.getDefaultAddress(user.getId()));
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    /**
     * 根据用户ID，查询该用户的所有收货地址
     * @param httpServletRequest HttpServlet请求体
     * @return 包含地址以及结果状态的JSONObject
     */
    @GetMapping("/address")
    public JSONObject getAllAddresses(HttpServletRequest httpServletRequest) {
        try {
            PinUser user = userService.whoAmI(httpServletRequest);
            JSONObject data = new JSONObject();
            data.put("list", addressService.getAllAddresses(user.getId()));
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    /**
     * @param httpServletRequest HttpServlet请求体
     * @param requestJSON 包含传入的地址
     * @return 创建地址状态
     * @author flyhero
     * 创建地址，增加了对isDefault的检查
     */
    @PostMapping("/address")
    public JSONObject createAddress(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        try {
            PinUser user = userService.whoAmI(httpServletRequest);
            PinUserAddress address = JSONObject.toJavaObject(requestJSON, PinUserAddress.class);
            address.setUserId(user.getId());
            address.setCreateTime(new Date());
            addressService.createAddress(address);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS,
                    null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    /**
     * @param httpServletRequest HttpServlet请求体
     * @param requestJSON 请求体JSON，包含地址ID
     * @return 删除结果状态 JSON
     * @author flyhero
     * 删除地址，会检查是否删除了默认地址
     */
    @DeleteMapping("/address")
    public JSONObject deleteAddress(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        try {
            PinUser user = userService.whoAmI(httpServletRequest);
            Integer addressId = requestJSON.getInteger("id");
            if (addressId == null) {
                return ResponseWrapper.wrap(PinConstants.StatusCode.PERMISSION_DENIED, "无权限删除", null);
            }
            int code = addressService.deleteAddress(addressId, user.getId());
            if (code == AddressService.STATUS_DELETE_ADDRESS_SUCCESS) {
                return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, null);
            } else if (code == AddressService.STATUS_DELETE_ADDRESS_INVALID_ID) {
                return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, "删除失败", null);
            } else if (code == AddressService.STATUS_DELETE_ADDRESS_PERMISSION_DENIED) {
                return ResponseWrapper.wrap(PinConstants.StatusCode.PERMISSION_DENIED, "无权限删除", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
        return null;
    }

    /**
     * @param httpServletRequest HttpServlet请求体
     * @param requestJSON 请求体JSON，包含地址信息
     * @return 更新结果状态 JSON
     * @author flyhero
     * 更新地址，会检查是否将其设置为默认地址，若是则替换掉原来的默认地址
     */
    @PutMapping("/address")
    public JSONObject updateAddress(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        try {
            PinUser user = userService.whoAmI(httpServletRequest);
            PinUserAddress addressToUpdate = JSONObject.toJavaObject(requestJSON, PinUserAddress.class);
            int code = addressService.updateAddress(user.getId(), addressToUpdate);
            if (code == AddressService.STATUS_UPDATE_ADDRESS_SUCCESS) { // 更新成功
                return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, null);
            } else if (code == AddressService.STATUS_UPDATE_ADDRESS_INVALID_ID) { // 无地址记录
                return ResponseWrapper.wrap(PinConstants.StatusCode.INVALID_DATA, "无对应地址记录！", null);
            } else if (code == AddressService.STATUS_UPDATE_ADDRESS_PERMISSION_DENIED) {
                return ResponseWrapper.wrap(PinConstants.StatusCode.PERMISSION_DENIED, "无权限删除！", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
        return null;
    }

    /**
     * @author flyhero
     * 获取商品浏览记录
     * @param httpServletRequest HttpServlet请求体
     * @return 用户商品浏览记录 JSON
     */
    @GetMapping("/product-visit-record")
    public JSONObject getUserProductRecord(HttpServletRequest httpServletRequest) {
        try {
            PinUser user = userService.whoAmI(httpServletRequest);
            JSONObject data = new JSONObject();
            data.put("list", userProductRecordService.getUserProductVisitRecord(user.getId()));
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
     * @param requestJSON 请求体JSON 包含要收藏的商品productId
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
     * @param productId 收藏的商品ID
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
     * @param requestJSON 请求体JSON
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
     * @param storeId 收藏的店铺ID
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

    /**
     * @param httpServletRequest HttpServlet请求体
     * @return 响应JSON
     * @author flyhero
     * 签到功能
     */
    @GetMapping("/check-in")
    public JSONObject checkIn(HttpServletRequest httpServletRequest) {
        PinUser user = userService.whoAmI(httpServletRequest);
        try {
            userCreditRecordService.dailyCheckIn(user.getId());
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, null);
        } catch (Exception e) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    /**
     * @param httpServletRequest HttpServlet请求体
     * @return 响应JSON
     * @author flyhero
     * 获取用户签到详细信息历史记录
     */
    @GetMapping("/credit-record")
    public JSONObject getUserCreditData(HttpServletRequest httpServletRequest) {
        PinUser user = userService.whoAmI(httpServletRequest);
        try {
            JSONObject data = userCreditRecordService.getUserCreditData(user.getId());
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    /**
     * @author flyhero
     * 判断某一用户今日是否已经签到
     * @param httpServletRequest HttpServlet请求体
     * @return 响应JSON
     */
    @GetMapping("/has-checked-in")
    public JSONObject hasCheckedIn(HttpServletRequest httpServletRequest) {
        PinUser user = userService.whoAmI(httpServletRequest);
        Boolean flag = userCreditRecordService.hasCheckedIn(user.getId());
        if (flag) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, "已签到", null);
        } else {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, "未签到", null);
        }
    }

    /**
     * @author flyhero
     * 为某一商品订单中的一组商品添加评论，同一订单只能添加一次
     * @param httpServletRequest HttpServlet请求体
     * @param requestJSON 包含一个对订单中每件sku的评论的数组，执行后将对其中每件商品都执行评论操作
     * @return 响应JSON
     */
    @RequestMapping("/add-comment")
    public JSONObject addComment(HttpServletRequest httpServletRequest, JSONObject requestJSON) {
        JSONArray jsonArray = requestJSON.getJSONArray("comments");
        List<PinUserProductComment> comments = jsonArray.toJavaList(PinUserProductComment.class);
        PinUser user = userService.whoAmI(httpServletRequest);
        try {
            productCommentService.addComment(user.getId(), comments);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, "评论成功！", null);
        } catch (PermissionDeniedException e) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.PERMISSION_DENIED, e.getMessage(), null);
        } catch (CommentFailedException e) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INVALID_DATA, e.getMessage(), null);
        }
    }

    /**
     * @author flyhero
     * 更新电话号码信息
     * @param httpServletRequest HttpServlet请求体
     * @param requestJSON 包含：phone: 待更新的电话号码
     * @return 响应JSON
     */
    @PostMapping("/update-phone")
    public JSONObject updatePhone(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        PinUser user = userService.whoAmI(httpServletRequest);
        String phone = requestJSON.getString("phone");
        Boolean updateSuccess = userService.updatePhone(user.getId(), phone);
        if(updateSuccess) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, "用户电话信息更新成功！", null);
        } else {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, PinConstants.ResponseMessage.INTERNAL_ERROR, null);
        }
    }

    /**
     * @author flyhero
     * 更新电子邮箱信息
     * @param httpServletRequest HttpServlet请求体
     * @param requestJSON 包含：email: 待更新的邮箱地址
     * @return 响应JSON
     */
    @PostMapping("/update-email")
    public JSONObject updateEmail(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        PinUser user = userService.whoAmI(httpServletRequest);
        String email = requestJSON.getString("email");
        Boolean updateSuccess = userService.updateEmail(user.getId(), email);
        if(updateSuccess) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, "用户邮箱信息更新成功！", null);
        } else {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, PinConstants.ResponseMessage.INTERNAL_ERROR, null);
        }
    }

    /**
     * @author flyhero
     * 更新密码
     * @param httpServletRequest HttpServlet请求体
     * @param requestJSON 包含password: 待更新的用户密码信息（传过来时是明文，在Service中再加密）
     * @return 响应JSON
     */
    @PostMapping("/update-password")
    public JSONObject updatePassword(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        PinUser user = userService.whoAmI(httpServletRequest);
        String password = requestJSON.getString("password");
        Boolean updateSuccess = userService.updatePassword(user.getId(), password);
        if(updateSuccess) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, "密码更新成功！", null);
        } else {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, PinConstants.ResponseMessage.INTERNAL_ERROR, null);
        }
    }

    /**
     * @author flyhero
     * 更新用户头像
     * @param httpServletRequest HttpServlet请求体
     * @param requestJSON 包含：avatarUrl: 待更新的头像图片链接地址
     * @return 响应JSON
     */
    @PostMapping("/update-avatar-url")
    public JSONObject updateAvatarUrl(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        PinUser user = userService.whoAmI(httpServletRequest);
        String avatarUrl = requestJSON.getString("avatarUrl");
        Boolean updateSuccess = userService.updateAvatarUrl(user.getId(), avatarUrl);
        if(updateSuccess) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, "头像更换成功！", null);
        } else {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, PinConstants.ResponseMessage.INTERNAL_ERROR, null);
        }
    }

    /**
     * @author flyhero
     * 更新一些常规信息
     * @param httpServletRequest HttpServlet请求体
     * @param requestJSON 包含需要更改的User信息的字段（不含手机，邮箱等等，前端限制）
     * @return 响应JSON
     */
    @PostMapping("/update-common-user-info")
    public JSONObject updateCommonUserInfo(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        PinUser user = userService.whoAmI(httpServletRequest);
        PinUser userInfoToUpdate = JSONObject.toJavaObject(requestJSON, PinUser.class);
        try {
            // 用户信息更新成功
            userService.updateCommonUserInfo(user.getId(), userInfoToUpdate);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, "用户信息更新成功！", null);
        } catch (Exception e) {
            // 发生未知错误，正常情况下不会进入
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, PinConstants.ResponseMessage.INTERNAL_ERROR, null);
        }
    }
}

