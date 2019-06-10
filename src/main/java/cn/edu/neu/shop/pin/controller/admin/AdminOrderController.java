package cn.edu.neu.shop.pin.controller.admin;

import cn.edu.neu.shop.pin.model.PinOrderIndividual;
import cn.edu.neu.shop.pin.service.ExpressService;
import cn.edu.neu.shop.pin.service.OrderIndividualService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminOrderController {

    @Autowired
    private ExpressService expressService;
    @Autowired
    OrderIndividualService orderIndividualService;

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

    @GetMapping("/order/query")
    public JSONObject getOrderByCondition(@RequestParam JSONObject queryType) {
        try {
            int pageNumber = queryType.getInteger("pageNumber");
            int pageSize = queryType.getInteger("pageSize");
            int orderTypeChoice = queryType.getInteger("orderTypeChoice");
            int orderDateChoice = queryType.getInteger("orderDateChoice");
            String keyWord = queryType.getString("keyWord");
            //查找得到所有的orders
            List<PinOrderIndividual> orderList = orderIndividualService.getAllWithProducts();
            //得到符合orderType的order
            List<PinOrderIndividual> orderTypeList = orderIndividualService.getOrdersByOrderType(orderList, orderTypeChoice);
            //得到符合orderDate的order
            List<PinOrderIndividual> orderDateList = orderIndividualService.getOrdersByOrderDate(orderTypeList, orderDateChoice,queryType);
            //通过key word查得符合的order
            List<PinOrderIndividual> orderKeyList = orderIndividualService.getOrdersByKeyWord(orderTypeList, keyWord);
            return ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, orderKeyList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, e.getMessage(), null);
        }
    }


}
