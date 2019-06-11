package cn.edu.neu.shop.pin.websocket;

import cn.edu.neu.shop.pin.model.PinOrderIndividual;
import cn.edu.neu.shop.pin.service.OrderGroupService;
import cn.edu.neu.shop.pin.service.OrderIndividualService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
public class GroupController {
//    private static Logger logger = Logger.getLogger()

    @Autowired
    OrderGroupService orderGroupService;
    @Autowired
    WebSocketService webSocketService;
    @Autowired
    OrderIndividualService orderIndividualService;

    @MessageMapping("/hello")
    public void hello(CustomerPrincipal customerPrincipal) {
        orderGroupService.sendGroupInitMessageToSingle(customerPrincipal);
    }

//    @MessageMapping("/remove/{individualOrderId}")
//    public void remove(CustomerPrincipal customerPrincipal, @DestinationVariable Integer individualOrderId) {
//        OrderIndividual orderIndividual = orderIndividualService.findById(individualOrderId);
//        if (orderIndividual == null || orderIndividual.getOrderGroupId() == null || !orderIndividual.getOrderGroupId().equals(customerPrincipal.getGroupOrderId()))
//            stompService.sendSingleErrorMessage(customerPrincipal, ErrorEnum.E_507);
//        else if (!orderGroupService.findById(customerPrincipal.getGroupOrderId()).getGroupOwnerId().equals(customerPrincipal.getCustomerId()))
//            stompService.sendSingleErrorMessage(customerPrincipal, ErrorEnum.E_501);
//        else {
//            if (GlobalData.lockAnIndividualOrder(individualOrderId)) {
//                if (orderIndividual.getIsPaid().equals(1)) {
//                    GlobalData.unlockAnIndividualOrder(individualOrderId);
//                    stompService.sendSingleErrorMessage(customerPrincipal, ErrorEnum.E_507);
//                    return;
//                }
//                GlobalData.unlockAnIndividualOrder(individualOrderId);
//                orderIndividualService.kickOutAOrder(individualOrderId);
//                stompService.sendBeenKickedOut(orderIndividual.getCustomerId());
//                stompService.sendSingleNotifyMessage(customerPrincipal, "踢出成功");
//                stompService.sendRoomNotifyMessage(customerPrincipal, "有人被移出了当前房间");
//                stompService.sendRoomHelloMessage(customerPrincipal);
//            } else
//                stompService.sendSingleErrorMessage(customerPrincipal, ErrorEnum.E_508);
//        }
//    }

    @MessageMapping("/join")
    public void join(CustomerPrincipal customerPrincipal) {
        PinOrderIndividual orderIndividual = orderIndividualService.findById(customerPrincipal.getOrderIndividualId());
        Integer orderGroupId = customerPrincipal.getOrderGroupId();
        webSocketService.sendGroupNotifyMessage(customerPrincipal, "有人适才加入了房间");
    }

    @MessageMapping("/out")
    public void out(CustomerPrincipal customerPrincipal) {
        PinOrderIndividual orderIndividual = orderIndividualService.findById(customerPrincipal.getOrderIndividualId());
        if (orderGroupService.findById(customerPrincipal.getOrderGroupId()).getOwnerUserId().equals(customerPrincipal.getUserId())) {
            webSocketService.sendSingleErrorMessage(customerPrincipal,
                    ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, "房主无法退出房间！", null));
        }
        else {
            if(!orderIndividual.getPaid()) {
                webSocketService.sendSingleErrorMessage(customerPrincipal,
                        ResponseWrapper.wrap(PinConstants.StatusCode.INTERNAL_ERROR, "订单尚未支付！", null));
                return;
            }
            webSocketService.getOut(customerPrincipal.getUserId());
            webSocketService.sendGroupNotifyMessage(customerPrincipal, "有人退出了当前房间！");
            webSocketService.sendGroupHelloMessage(customerPrincipal);
//            if (GlobalData.lockAnIndividualOrder(orderIndividual.getId())) {
//                if (orderIndividual.getIsPaid().equals(1)) {
//                    GlobalData.unlockAnIndividualOrder(orderIndividual.getId());
//                    stompService.sendSingleErrorMessage(customerPrincipal, ErrorEnum.E_507);
//                    return;
//                }
//                GlobalData.unlockAnIndividualOrder(orderIndividual.getId());
                orderIndividualService.kickOutAnOrder(orderIndividual.getId());
//                stompService.getOut(orderIndividual.getCustomerId());
//                stompService.sendRoomNotifyMessage(customerPrincipal, "有人退出了当前房间");
//                stompService.sendRoomHelloMessage(customerPrincipal);
//            } else
//                stompService.sendSingleErrorMessage(customerPrincipal, ErrorEnum.E_508);
        }
    }
}
