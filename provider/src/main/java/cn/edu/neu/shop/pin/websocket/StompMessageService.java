package cn.edu.neu.shop.pin.websocket;

import cn.edu.neu.shop.pin.message_queue.producer.MessageOnQueueProducer;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

/**
 * @author flyhero, LLG
 */
@Component
public class StompMessageService {

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    private final MessageOnQueueProducer messageOnQueueProducer;

    @Autowired
    public StompMessageService(SimpMessageSendingOperations simpMessageSendingOperations, MessageOnQueueProducer messageOnQueueProducer) {
        this.simpMessageSendingOperations = simpMessageSendingOperations;
        this.messageOnQueueProducer = messageOnQueueProducer;
    }

    /**
     * @param principal 用户principal
     * @param errorInfo 错误信息
     * @author flyhero
     * 点对点发送错误信息
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
     * @param principal  用户principal
     * @param jsonObject 需要发送的信息JSON
     * @author flyhero
     * 点对点发送通知信息
     */
    public void sendSingleUpdateMessage(CustomerPrincipal principal, JSONObject jsonObject) {
        sendSingleMessage(principal, "update", jsonObject);
    }

    /**
     * @param principal  用户principal
     * @param jsonObject 需要发送的信息JSON
     * @author flyhero
     * 在同一个Group内发送通知信息
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
     * @param principal 用户principal
     * @param router    路由地址
     * @param data    待传数据
     * @author flyhero
     * 点对点发送消息
     */
    private void sendSingleMessage(CustomerPrincipal principal, String router, JSONObject data) {
        sendToUserRoute(principal.getUserId().toString(), "/" + router, data);
    }

    /**
     * @param principal 用户principal
     * @param router    路由地址
     * @param data    待传数据
     * @author flyhero
     * 向/group/{orderGroupId}/notify地址发送广播消息，属于同一团内的用户订阅这个地址
     */
    private void sendGroupMessage(CustomerPrincipal principal, String router, JSONObject data) {
        sendToGroupRoute("/group/" + principal.getOrderGroupId() + "/" + router, data);
    }

    /**
     * @author LLG
     * 向个人用户发送信息：抽象分离方法
     * 向消息队列转发 stomp 信息
     * @param userId 用户Id
     * @param route 路由key
     * @param data 数据
     */
    private void sendToUserRoute(String userId, String route, JSONObject data) {
        messageOnQueueProducer.sendToUser(userId, route, data);
    }

    /**
     * @author LLG
     * 向组内用户发送信息：抽象分离方法
     * 向消息队列转发 stomp 信息
     * @param route 路由key
     * @param data 数据
     */
    private void sendToGroupRoute(String route, JSONObject data) {
        messageOnQueueProducer.sendToGroup(route, data);
    }

    /**
     * 处理消息队列的组内消息回调方法
     * @param route 路由
     * @param data 数据
     */
    public void onReceiveGroupMessageToRoute(String route, JSONObject data) {
        simpMessageSendingOperations.convertAndSend(route, data);
    }

    /**
     * 处理消息队列的用户个人消息回调方法
     * @param userId 用户Id
     * @param route 路由
     * @param data 数据
     */
    public void onReceiveUserMessageToRoute(String userId, String route, JSONObject data) {
        simpMessageSendingOperations.convertAndSendToUser(userId, route, data);
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
