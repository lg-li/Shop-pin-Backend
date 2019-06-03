package cn.edu.neu.shop.pin.websocket;

import cn.edu.neu.shop.pin.util.PinConstants;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.ArrayList;

/**
 * @author flyhero
 * @Description WebSocket控制器
 */
@Controller
public class WebSocketController {
    @MessageMapping(PinConstants.FORE_TO_SERVER_PATH)//当浏览器向服务端发送请求时，通过@MessageMapping映射/welcome这个地址，类似于@RequestMapping
    @SendTo(PinConstants.PRODUCER_PATH)//当服务端有消息时，会对订阅了@SendTo中的路径的浏览器发送消息
    public WiselyResponse say(WiselyMessage message) throws Exception {
        List<String> users = new ArrayList<>();
        return new WiselyResponse("Welcome, " + message.getName() + "!");
    }
}
