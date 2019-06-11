package cn.edu.neu.shop.pin.controller.realtimecommunication;

import cn.edu.neu.shop.pin.schedule.GroupClosingScheduler;
import cn.edu.neu.shop.pin.service.OrderGroupService;
import cn.edu.neu.shop.pin.service.OrderIndividualService;
import cn.edu.neu.shop.pin.websocket.CustomerPrincipal;
import cn.edu.neu.shop.pin.websocket.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@MessageMapping("/customer")
public class GroupController {

    private static Logger logger = LoggerFactory.getLogger(GroupController.class);

    @Autowired
    OrderGroupService orderGroupService;
    @Autowired
    WebSocketService webSocketService;
    @Autowired
    OrderIndividualService orderIndividualService;

    @MessageMapping("/hello")
    public void hello(CustomerPrincipal customerPrincipal) {
        logger.info("Got Hello from user #" + customerPrincipal.getUserId());
        orderGroupService.sendGroupInitMessageToSingle(customerPrincipal);
    }

//    @MessageMapping("/out")
//    public void out(CustomerPrincipal customerPrincipal) {
//        PinOrderIndividual orderIndividual = orderIndividualService.findById(customerPrincipal.getOrderIndividualId());
//        if (orderGroupService.findById(customerPrincipal.getOrderGroupId()).getOwnerUserId().equals(customerPrincipal.getUserId())) {
//            webSocketService.sendSingleErrorMessage(customerPrincipal,
//                    ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, "房主无法退出房间！", null));
//        }
//        else {
//            if(!orderIndividual.getPaid()) {
//                webSocketService.sendSingleErrorMessage(customerPrincipal,
//                        ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, "订单尚未支付！", null));
//                return;
//            }
//            webSocketService.getOut(customerPrincipal.getUserId());
//            webSocketService.sendGroupNotifyMessage(customerPrincipal, "有人退出了当前房间！");
//            webSocketService.sendGroupHelloMessage(customerPrincipal);
//                orderIndividualService.kickOutAnOrder(orderIndividual.getId());
//        }
//    }
}
