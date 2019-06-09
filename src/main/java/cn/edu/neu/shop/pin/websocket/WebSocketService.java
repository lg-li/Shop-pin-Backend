package cn.edu.neu.shop.pin.websocket;

import cn.edu.neu.shop.pin.model.PinOrderIndividual;
import cn.edu.neu.shop.pin.model.PinOrderItem;
import cn.edu.neu.shop.pin.service.AddressService;
import cn.edu.neu.shop.pin.service.OrderGroupService;
import cn.edu.neu.shop.pin.service.OrderIndividualService;
import cn.edu.neu.shop.pin.service.security.UserService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author flyhero
 */
@Component
public class WebSocketService {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    @Autowired
    private OrderGroupService orderGroupService;

    @Autowired
    private OrderIndividualService orderIndividualService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserService userService;

    /**
     * @author flyhero
     * 用户被踢出房间
     * @param userId
     */
    public void sendBeenKickedOut(Integer userId) {
        simpMessageSendingOperations.convertAndSendToUser(userId.toString(), "/kick",
                ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, null));
    }

    /**
     * @author flyhero
     * 用户主动退出
     * @param userId
     */
    public void getOut(Integer userId) {
        simpMessageSendingOperations.convertAndSendToUser(userId.toString(), "/out",
                ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, null));
    }

    /**
     * @author flyhero
     * 超时
     * @param customerId
     */
    public void sendTimeout(Integer customerId) {
        simpMessageSendingOperations.convertAndSendToUser(customerId.toString(), "/time-out",
                ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, null));
    }

    /**
     * @author flyhero
     * 点对点发送错误信息
     * @param principal
     * @param errorInfo
     */
    public void sendSingleErrorMessage(CustomerPrincipal principal, JSONObject errorInfo) {
        sendSingleMessage(principal, "error", errorInfo);
    }

    /**
     * @author flyhero
     * 向同一Group内的用户发送错误信息
     * @param principal
     * @param errorInfo
     */
    public void sendGroupErrorMessage(CustomerPrincipal principal, JSONObject errorInfo) {
        sendGroupMessage(principal, "error", errorInfo);

    }

    /**
     * @author flyhero
     * 点对点发送通知信息
     * @param principal
     * @param msg
     */
    public void sendSingleNotifyMessage(CustomerPrincipal principal, String msg) {
        sendSingleMessage(principal, "notify",
                ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, msg, null));
    }

    /**
     * @author flyhero
     * 在同一个Group内发送通知信息
     * @param principal
     * @param msg
     */
    public void sendGroupNotifyMessage(CustomerPrincipal principal, String msg) {
        sendGroupMessage(principal, "notify",
                ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, msg, null));
    }

    /**
     * @author flyhero
     * 点对点发送Hello消息
     * @param principal
     */
    public void sendSingleHelloMessage(CustomerPrincipal principal, JSONObject jsonObject) {
        sendSingleMessage(principal, "hello", jsonObject);
    }

    /**
     * @author flyhero
     * 向同一Group内的用户发送Hello消息
     * @param newPrincipal
     */
    public void sendGroupHelloMessage(CustomerPrincipal newPrincipal) {
        List<PinOrderIndividual> orderIndividuals = orderIndividualService.getOrderIndividualsByOrderGroupId(newPrincipal.getOrderGroupId());
        orderIndividuals.forEach(orderIndividual -> {
            CustomerPrincipal principal = new CustomerPrincipal(orderIndividual.getUserId(), orderIndividual.getId(), orderIndividual.getOrderGroupId());
            orderGroupService.sendGroupInitMessageToSingle(principal);
        });
    }

    /**
     * @author flyhero
     * 发送结束消息
     * @param orderGroupId
     * @param finalPeopleCount
     */
    public void sendFinishedMessage(Integer orderGroupId, Integer finalPeopleCount) {
        JSONObject returnData = new JSONObject();
        returnData.put("finalPeopleCount", finalPeopleCount);
        sendGroupMessage(orderGroupId, "notify",
                ResponseWrapper.wrap(PinConstants.StatusCode.SUCCESS, PinConstants.ResponseMessage.SUCCESS, returnData));
    }

    /**
     * @author flyhero
     * 在一个Group内发送广播消息
     * @param orderGroupId
     * @param router
     * @param object
     */
    private void sendGroupMessage(Integer orderGroupId, String router, Object object) {
        simpMessageSendingOperations.convertAndSend("/group/" + orderGroupId + "/" + router, object);
    }

    /**
     * @author flyhero
     * 在一个Group内发送广播消息
     * @param principal
     * @param router
     * @param object
     */
    private void sendGroupMessage(CustomerPrincipal principal, String router, Object object) {
        simpMessageSendingOperations.convertAndSend("/group/" + principal.getOrderGroupId() + "/" + router, object);
    }

    /**
     * @author flyhero
     * 点对点发送消息
     * @param principal
     * @param router
     * @param object
     */
    private void sendSingleMessage(CustomerPrincipal principal, String router, Object object) {
        simpMessageSendingOperations.convertAndSendToUser(principal.getUserId().toString(), "/" + router, object);
    }
}
