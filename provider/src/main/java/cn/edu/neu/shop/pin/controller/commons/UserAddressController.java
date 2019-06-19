package cn.edu.neu.shop.pin.controller.commons;


import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.model.PinUserAddress;
import cn.edu.neu.shop.pin.service.AddressService;
import cn.edu.neu.shop.pin.service.security.UserService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 用户地址相关操作 controller
 */
@RestController
@RequestMapping("/commons/user")
public class UserAddressController {

    private final UserService userService;

    private final AddressService addressService;

    @Autowired
    public UserAddressController(UserService userService, AddressService addressService) {
        this.userService = userService;
        this.addressService = addressService;
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
     *
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
     * @param requestJSON        包含传入的地址
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
     * @param requestJSON        请求体JSON，包含地址ID
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
     * @param requestJSON        请求体JSON，包含地址信息
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
}
