package cn.edu.neu.shop.pin.controller.admin;

import cn.edu.neu.shop.pin.model.PinStore;
import cn.edu.neu.shop.pin.model.PinStoreGroupCloseBatch;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.service.StoreCloseBatchService;
import cn.edu.neu.shop.pin.service.StoreService;
import cn.edu.neu.shop.pin.service.security.UserService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import cn.edu.neu.shop.pin.util.img.ImgUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/manager/store")
public class AdminStoreController {

    @Autowired
    UserService userService;
    @Autowired
    StoreService storeService;

    @Autowired
    StoreCloseBatchService storeCloseBatchService;

    /**
     * 得到这个商人所有的商铺
     *
     * @param req 传入的request
     * @return 返回所有的商铺
     */
    @GetMapping("/storeList")
    public JSONObject getProducts(HttpServletRequest req) {
        try {
            PinUser user = userService.whoAmI(req);
            JSONObject data = new JSONObject();
            data.put("storeList", storeService.getStoreListByOwnerId(user.getId()));
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    /**
     * 新增店铺
     */
    @PostMapping("/storeInfo")
    public JSONObject addStoreInfo(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        try {
            PinUser user = userService.whoAmI(httpServletRequest);
            String storeName = requestJSON.getString("name");
            String description = requestJSON.getString("description");
            String phone = requestJSON.getString("phone");
            String email = requestJSON.getString("email");
            String logoUrl = requestJSON.getString("logoUrl");
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS,
                    storeService.addStoreInfo(storeName, description, phone, email, logoUrl, user.getId()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    /**
     * 修改店铺信息
     *
     * @param httpServletRequest
     * @param requestJSON
     * @return
     */
    @PutMapping("/storeInfo")
    public JSONObject updateStoreInfo(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        try {
            PinUser user = userService.whoAmI(httpServletRequest);
            PinStore store = JSONObject.toJavaObject(requestJSON, PinStore.class);
            if (storeService.update(store) == null) {
                return ResponseWrapper.wrap(PinConstants.StatusCode.PERMISSION_DENIED, PinConstants.ResponseMessage.PERMISSION_DENIED, null);
            }
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    @GetMapping("/close-batch")
    public JSONObject getGruopCloseBatchTime(HttpServletRequest httpServletRequest) {
        try{
            String store = httpServletRequest.getHeader("current-store");
            Integer storeId = Integer.parseInt(store);
            JSONObject data = new JSONObject();
            data.put("list", storeCloseBatchService.getGroupCloseBatchTime(storeId));
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
        } catch (Exception e) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<JSONObject> uploadStoreInfo(HttpServletRequest httpServletRequest, @RequestBody JSONObject uploadingInfo){
        //截掉 "data:image/png;base64,"
        String base64Img = uploadingInfo.getString("image").substring(22);
        return ImgUtil.upload(base64Img,"https://sm.ms/api/upload");
    }

    @DeleteMapping("/close-batch")
    public JSONObject deleteGroupCloseBatchTime(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        try {
            Integer storeId = Integer.valueOf(httpServletRequest.getHeader("Current-Store"));
            JSONArray array = requestJSON.getJSONArray("closeBatchList");
            for (int i = 0; i < array.size(); i++) {
                Integer id = array.getJSONObject(i).getInteger("id");
                storeCloseBatchService.deleteGroupCloseBatch(storeId, id);
            }
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, null);
        } catch (Exception e) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    @PostMapping("/close-batch")
    public JSONObject addGroupCloseBatchTime(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        try{
            Integer storeId = Integer.valueOf(httpServletRequest.getHeader("Current-Store"));
            Date date = requestJSON.getDate("time");
            List<PinStoreGroupCloseBatch> list = storeCloseBatchService.addGroupCloseBatch(storeId, date);
            JSONObject data = new JSONObject();
            data.put("closeBatch", data);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
        } catch (Exception e) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }
}
