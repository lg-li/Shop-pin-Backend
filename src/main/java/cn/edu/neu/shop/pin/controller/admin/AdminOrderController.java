package cn.edu.neu.shop.pin.controller.admin;

import cn.edu.neu.shop.pin.model.PinOrderGroup;
import cn.edu.neu.shop.pin.model.PinOrderIndividual;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.service.ExpressService;
import cn.edu.neu.shop.pin.service.OrderGroupService;
import cn.edu.neu.shop.pin.service.OrderIndividualService;
import cn.edu.neu.shop.pin.service.security.UserService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminOrderController {

    @Autowired
    private ExpressService expressService;
    @Autowired
    OrderIndividualService orderIndividualService;
    @Autowired
    OrderGroupService orderGroupService;
    @Autowired
    UserService userService;

    @GetMapping("/order/deliverNameList")
    public JSONObject getExpressInfo() {
        try {
            JSONObject data = new JSONObject();
            data.put("list", expressService.getExpressInfo());
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    @PostMapping("/order/query")
    public JSONObject getOrderByCondition(HttpServletRequest req, @RequestBody JSONObject queryType) {
        try {
            System.out.println(queryType);
            Integer storeId = Integer.parseInt(req.getHeader("Current-Store"));
            Integer pageNumber = queryType.getInteger("pageNumber");
            Integer pageSize = queryType.getInteger("pageSize");
            Integer orderTypeChoice = queryType.getInteger("orderTypeChoice");
            Integer orderDateChoice = queryType.getInteger("orderDateChoice");
            String keyWord = queryType.getString("key");
            //通过关键词查找得到所有的orders
            List<PinOrderIndividual> orderList = orderIndividualService.getAllWithProductsByKeyWord(keyWord,storeId);
            //得到符合orderType的order
            List<PinOrderIndividual> orderTypeList = orderIndividualService.getOrdersByOrderType(orderList, orderTypeChoice);
            //得到符合orderDate的order
            List<PinOrderIndividual> orderDateList = orderIndividualService.getOrdersByOrderDate(orderTypeList, orderDateChoice, queryType);
            //通过传入的一页的size和页码，返回那一页的list
            List<PinOrderIndividual> list = (List<PinOrderIndividual>) orderIndividualService.getOrdersByPageNumAndSize(orderDateList, pageNumber, pageSize);

            JSONObject specificPage = new JSONObject();
            specificPage.put("total", orderDateList.size());
            specificPage.put("orderList", list);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, specificPage);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    @PostMapping("/order/get-group-order-list")
    public JSONObject getGroupOrderByCondition(HttpServletRequest req,@RequestBody JSONObject queryType) {
        try {
            System.out.println(queryType);
            Integer storeId = Integer.parseInt(req.getHeader("Current-Store"));
            Integer pageNumber = queryType.getInteger("pageNumber");
            Integer pageSize = queryType.getInteger("pageSize");
            Integer groupStatus = queryType.getInteger("groupStatus");
            Date begin = queryType.getDate("begin");
            Date end = queryType.getDate("end");
            //得到所有的orderGroup，其中嵌套了orderIndividual,orderIndividual中也嵌套了其他信息
            List<PinOrderGroup> orderGroups = orderGroupService.getAllWithOrderIndividual(storeId);
            //得到符合传入的状态的orderGroups
            List<PinOrderGroup> orderStatusGroups = orderGroupService.getOrdersByOrderStatus(orderGroups, groupStatus);
            //得到符合传入时间段的orderGroups
            List<PinOrderGroup> orderDateGroups = orderGroupService.getOrdersByDate(orderStatusGroups, begin, end);
            //为前端分页处理
            List<PinOrderIndividual> list = (List<PinOrderIndividual>) orderIndividualService.getOrdersByPageNumAndSize(orderDateGroups, pageNumber, pageSize);

            JSONObject specificPage = new JSONObject();
            specificPage.put("total", orderDateGroups.size());
            specificPage.put("orderList", list);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, specificPage);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }

    @PutMapping("/order")
    public JSONObject updateProductStatueToShip(HttpServletRequest httpServletRequest, @RequestBody JSONObject requestJSON) {
        try{
            PinUser user = userService.whoAmI(httpServletRequest);
            Integer orderIndividualId = requestJSON.getInteger("orderIndividualId");
            String deliveryType = requestJSON.getString("deliveryType");
            Date date = new Date();
            switch (deliveryType) {
                case "ONLINE":
                    orderIndividualService.updateOrderStatusNotExpress(orderIndividualId, deliveryType, date);
                    return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, null);
                case "EXPRESS":
                    String deliveryName = requestJSON.getString("deliveryName");
                    Integer deliveryId = requestJSON.getInteger("deliveryId");
                    orderIndividualService.updateOrderStatusIsExpress(orderIndividualId, deliveryType, deliveryName, deliveryId, date);
                    return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, null);
                case "OFFLINE":
                    orderIndividualService.updateOrderStatusNotExpress(orderIndividualId, deliveryType, date);
                    return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, null);
                default:
                    return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, "发货失败", null);
            }
        } catch (Exception e) {
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }
}
