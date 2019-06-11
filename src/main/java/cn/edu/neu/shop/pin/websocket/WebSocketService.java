package cn.edu.neu.shop.pin.websocket;

import cn.edu.neu.shop.pin.model.PinOrderGroup;
import cn.edu.neu.shop.pin.model.PinOrderIndividual;
import cn.edu.neu.shop.pin.model.PinOrderItem;
import cn.edu.neu.shop.pin.service.AddressService;
import cn.edu.neu.shop.pin.service.OrderGroupService;
import cn.edu.neu.shop.pin.service.OrderIndividualService;
import cn.edu.neu.shop.pin.service.security.UserService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.ResponseWrapper;
import com.alibaba.fastjson.JSONArray;
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
     * 点对点发送错误信息
     * @param principal
     * @param errorInfo
     */
    public void sendSingleErrorMessage(CustomerPrincipal principal, JSONObject errorInfo) {
        sendSingleMessage(principal, "error", errorInfo);
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
     * 向/group/{orderGroupId}/notify地址发送广播消息，属于同一团内的用户订阅这个地址
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
        simpMessageSendingOperations.convertAndSendToUser("/user/" + principal.getUserId().toString(), "/" + router, object);
    }

    private void sendSingleMerchant(MerchantPrincipal merchantPrincipal, String router, Object o) {
        simpMessageSendingOperations.convertAndSend("/provider/" + merchantPrincipal.getStoreId() + '/' + router, o);
    }
}
