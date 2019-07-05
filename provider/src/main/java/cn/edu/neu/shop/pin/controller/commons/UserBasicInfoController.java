package cn.edu.neu.shop.pin.controller.commons;

import cn.edu.neu.shop.pin.exception.CommentFailedException;
import cn.edu.neu.shop.pin.exception.PermissionDeniedException;
import cn.edu.neu.shop.pin.mapper.PinSettingsConstantMapper;
import cn.edu.neu.shop.pin.model.*;
import cn.edu.neu.shop.pin.service.ProductCommentService;
import cn.edu.neu.shop.pin.service.UserCreditRecordService;
import cn.edu.neu.shop.pin.service.UserProductRecordService;
import cn.edu.neu.shop.pin.service.WechatUserService;
import cn.edu.neu.shop.pin.service.message.TemplateMessageService;
import cn.edu.neu.shop.pin.service.security.UserService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/commons/user")
public class UserBasicInfoController {

    private final UserService userService;

    private final UserProductRecordService userProductRecordService;

    private final ProductCommentService productCommentService;

    private final UserCreditRecordService userCreditRecordService;

    private final PinSettingsConstantMapper pinSettingsConstantMapper;

    @Autowired
    private WechatUserService wechatUserService;

    @Autowired
    private TemplateMessageService templateMessageService;

    @Autowired
    public UserBasicInfoController(UserService userService, UserProductRecordService userProductRecordService, ProductCommentService productCommentService, UserCreditRecordService userCreditRecordService, PinSettingsConstantMapper pinSettingsConstantMapper) {
        this.userService = userService;
        this.userProductRecordService = userProductRecordService;
        this.productCommentService = productCommentService;
        this.userCreditRecordService = userCreditRecordService;
        this.pinSettingsConstantMapper = pinSettingsConstantMapper;
    }

    /**
     * 获取用户信息
     *
     * @param httpServletRequest HttpServlet请求体
     * @return 包含用户信息以及结果状态的JSONObject
     */
    @GetMapping("/info")
    public JSONObject getUserInfo(HttpServletRequest httpServletRequest) {
        try {
            PinUser user = userService.whoAmI(httpServletRequest);
            Boolean hasCheckedIn = userCreditRecordService.hasCheckedIn(user.getId());
            JSONObject data = new JSONObject();
            data.put("user", user);
            data.put("hasCheckedIn", hasCheckedIn);
            PinSettingsConstant pinSettingsConstant = pinSettingsConstantMapper.selectByPrimaryKey("banner_content");
            data.put("banner", pinSettingsConstant.getConstantValue());
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS,
                    data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    /**
     * @param httpServletRequest HttpServlet请求体
     * @return 用户商品浏览记录 JSON
     * @author flyhero
     * 获取商品浏览记录
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
     * @param httpServletRequest HttpServlet请求体
     * @return 响应JSON
     * @author flyhero
     * 判断某一用户今日是否已经签到
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
     * @param httpServletRequest HttpServlet请求体
     * @param requestJSON        包含一个对订单中每件sku的评论的数组，执行后将对其中每件商品都执行评论操作
     * @return 响应JSON
     * @author flyhero
     * 为某一商品订单中的一组商品添加评论，同一订单只能添加一次
     */
    @PostMapping("/add-comment")
    public JSONObject addComment(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        PinUser user = userService.whoAmI(httpServletRequest);
        JSONArray jsonArray = requestJSON.getJSONArray("comments");
        List<PinUserProductComment> comments = new ArrayList<>();
        // 预处理数据
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jo = jsonArray.getJSONObject(i);
            PinUserProductComment comment = new PinUserProductComment();
            comment.setUserId(user.getId());
            comment.setOrderIndividualId(jo.getInteger("orderIndividualId"));
            comment.setSkuId(jo.getInteger("skuId"));
            comment.setGrade(jo.getInteger("grade"));
            comment.setProductScore(jo.getInteger("productScore"));
            comment.setServiceScore(jo.getInteger("serviceScore"));
            comment.setContent(jo.getString("content"));
            comment.setImagesUrls(jo.getString("imagesUrls"));
            comments.add(comment);
        }
        try {
            productCommentService.addComments(user.getId(), comments);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, "评论成功！", null);
        } catch (PermissionDeniedException e) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.PERMISSION_DENIED, e.getMessage(), null);
        } catch (CommentFailedException e) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INVALID_DATA, e.getMessage(), null);
        }
    }

    /**
     * @param httpServletRequest HttpServlet请求体
     * @param requestJSON        包含：phone: 待更新的电话号码
     * @return 响应JSON
     * @author flyhero
     * 更新电话号码信息
     */
    @PostMapping("/update-phone")
    public JSONObject updatePhone(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        PinUser user = userService.whoAmI(httpServletRequest);
        String phone = requestJSON.getString("phone");
        Boolean updateSuccess = userService.updatePhone(user.getId(), phone);
        if (updateSuccess) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, "用户电话信息更新成功！", null);
        } else {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, PinConstants.ResponseMessage.INTERNAL_ERROR, null);
        }
    }

    /**
     * @param httpServletRequest HttpServlet请求体
     * @param requestJSON        包含：email: 待更新的邮箱地址
     * @return 响应JSON
     * @author flyhero
     * 更新电子邮箱信息
     */
    @PostMapping("/update-email")
    public JSONObject updateEmail(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        PinUser user = userService.whoAmI(httpServletRequest);
        String email = requestJSON.getString("email");
        Boolean updateSuccess = userService.updateEmail(user.getId(), email);
        if (updateSuccess) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, "用户邮箱信息更新成功！", null);
        } else {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, PinConstants.ResponseMessage.INTERNAL_ERROR, null);
        }
    }

    /**
     * @param httpServletRequest HttpServlet请求体
     * @param requestJSON        包含password: 待更新的用户密码信息（传过来时是明文，在Service中再加密）
     * @return 响应JSON
     * @author flyhero
     * 更新密码
     */
    @PostMapping("/update-password")
    public JSONObject updatePassword(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        PinUser user = userService.whoAmI(httpServletRequest);
        String password = requestJSON.getString("password");
        Boolean updateSuccess = userService.updatePassword(user.getId(), password);
        if (updateSuccess) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, "密码更新成功！", null);
        } else {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, PinConstants.ResponseMessage.INTERNAL_ERROR, null);
        }
    }

    /**
     * @param httpServletRequest HttpServlet请求体
     * @param requestJSON        包含：avatarUrl: 待更新的头像图片链接地址
     * @return 响应JSON
     * @author flyhero
     * 更新用户头像
     */
    @PostMapping("/update-avatar-url")
    public JSONObject updateAvatarUrl(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        PinUser user = userService.whoAmI(httpServletRequest);
        String avatarUrl = requestJSON.getString("avatarUrl");
        Boolean updateSuccess = userService.updateAvatarUrl(user.getId(), avatarUrl);
        if (updateSuccess) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, "头像更换成功！", null);
        } else {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, PinConstants.ResponseMessage.INTERNAL_ERROR, null);
        }
    }

    /**
     * @param httpServletRequest HttpServlet请求体
     * @param requestJSON        包含需要更改的User信息的字段（不含手机，邮箱等等，前端限制）
     * @return 响应JSON
     * @author flyhero
     * 更新一些常规信息
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

    @PostMapping("/submit-form-id")
    public JSONObject submitFormID(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        String formId = requestJSON.getString("formId");
        if (formId == null) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, PinConstants.ResponseMessage.INTERNAL_ERROR, null);
        }
        try {
            PinUser user = userService.whoAmI(httpServletRequest);
            PinWechatUser wechatUser = wechatUserService.findWechatUserByUserId(user.getId());
            if(wechatUser == null) {
                return ResponseWrapper.wrap(PinConstants.StatusCode.INVALID_DATA, "当前用户没有微信绑定", null);
            }
            PinMiniProgramFormIdRecord newRecord = new PinMiniProgramFormIdRecord();
            newRecord.setCreateTime(new Date());
            newRecord.setFormId(formId);
            newRecord.setWechatUserId(wechatUser.getId());
            return ResponseWrapper.wrap(
                    PinConstants.StatusCode.SUCCESS,
                    "Form ID 提交成功",
                    templateMessageService.save(newRecord));
        } catch (Exception e) {
            // 发生未知错误，正常情况下不会进入
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, PinConstants.ResponseMessage.INTERNAL_ERROR, e.getMessage());
        }
    }
}
