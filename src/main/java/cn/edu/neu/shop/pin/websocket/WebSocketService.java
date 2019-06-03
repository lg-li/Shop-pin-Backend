package cn.edu.neu.shop.pin.websocket;

import cn.edu.neu.shop.pin.util.PinConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

/**
 * @author flyhero
 *
 */
public class WebSocketService {

    @Autowired
    private SimpMessagingTemplate template;

    public void sendPublicMessage(WiselyResponse message) {
        template.convertAndSend(PinConstants.PRODUCER_PATH, message);
    }

    public void sendPrivateMessage(List<String> users, WiselyResponse message) {
        users.forEach(userName -> {
            template.convertAndSendToUser(userName, PinConstants.P2P_PUSH_PATH, message);
        });
    }
}
