package cn.edu.neu.shop.pin.websocket;

import com.alibaba.fastjson.JSONObject;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

/**
 * @author flyhero
 */
@Component
public class WebSocketService {

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    public WebSocketService(SimpMessageSendingOperations simpMessageSendingOperations) {
        this.simpMessageSendingOperations = simpMessageSendingOperations;
    }

    /**
     * @author flyhero
     * 点对点发送错误信息
     * @param principal 用户principal
     * @param errorInfo 错误信息
     */
    public void sendSingleErrorMessage(CustomerPrincipal principal, JSONObject errorInfo) {
        sendSingleMessage(principal, "error", errorInfo);
    }

//    /**
//     * @author flyhero
//     * 向同一Group内的用户发送错误信息
//     * @param principal 用户principal
//     * @param errorInfo 错误信息
//     */
//    public void sendGroupErrorMessage(CustomerPrincipal principal, JSONObject errorInfo) {
//        sendGroupMessage(principal, "error", errorInfo);
//
//    }

    /**
     * @author flyhero
     * 点对点发送通知信息
     * @param principal 用户principal
     * @param jsonObject 需要发送的信息JSON
     */
    public void sendSingleUpdateMessage(CustomerPrincipal principal, JSONObject jsonObject) {
        sendSingleMessage(principal, "update", jsonObject);
    }

    /**
     * @author flyhero
     * 在同一个Group内发送通知信息
     * @param principal 用户principal
     * @param jsonObject 需要发送的信息JSON
     */
    public void sendGroupUpdateMessage(CustomerPrincipal principal, JSONObject jsonObject) {
        sendGroupMessage(principal, "update", jsonObject);
    }

//    /**
//     * @author flyhero
//     * 点对点发送Hello消息
//     * @param principal 用户principle
//     */
//    public void sendSingleHelloMessage(CustomerPrincipal principal, JSONObject jsonObject) {
//        sendSingleMessage(principal, "hello", jsonObject);
//    }

//    /**
//     * @author flyhero
//     * 向同一Group内的用户发送Hello消息
//     * @param newPrincipal
//     */
//    public void sendGroupHelloMessage(CustomerPrincipal newPrincipal) {
//        List<PinOrderIndividual> orderIndividuals = orderIndividualService.getOrderIndividualsByOrderGroupId(newPrincipal.getOrderGroupId());
//        orderIndividuals.forEach(orderIndividual -> {
//            CustomerPrincipal principal = new CustomerPrincipal(orderIndividual.getUserId(), orderIndividual.getId(), orderIndividual.getOrderGroupId());
//            orderGroupService.sendGroupInitMessageToSingle(principal);
//        });
//    }

//    /**
//     * @author flyhero
//     * 发送结束消息
//     * @param orderGroupId
//     * @param finalPeopleCount
//     */
//    public void sendFinishedMessage(Integer orderGroupId, Integer finalPeopleCount) {
//        JSONObject returnData = new JSONObject();
//        returnData.put("finalPeopleCount", finalPeopleCount);
//        sendGroupMessage(orderGroupId, "notify",
//                ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, returnData));
//    }

    /**
     * @author flyhero
     * 点对点发送消息
     * @param principal 用户principal
     * @param router 路由地址
     * @param object 待传对象
     */
    private void sendSingleMessage(CustomerPrincipal principal, String router, Object object) {
        simpMessageSendingOperations.convertAndSendToUser(principal.getUserId().toString(), "/" + router, object);
    }

    /**
     * @author flyhero
     * 向/group/{orderGroupId}/notify地址发送广播消息，属于同一团内的用户订阅这个地址
     * @param principal 用户principal
     * @param router 路由地址
     * @param object 待传对象
     */
    private void sendGroupMessage(CustomerPrincipal principal, String router, Object object) {
        simpMessageSendingOperations.convertAndSend("/group/" + principal.getOrderGroupId() + "/" + router, object);
    }

//    /**
//     * @author flyhero
//     * 在一个Group内发送广播消息
//     * @param orderGroupId 团单ID
//     * @param router 路由地址
//     * @param object 待传对象
//     */
//    private void sendGroupMessage(Integer orderGroupId, String router, Object object) {
//        simpMessageSendingOperations.convertAndSend("/group/" + orderGroupId + "/" + router, object);
//    }
}
