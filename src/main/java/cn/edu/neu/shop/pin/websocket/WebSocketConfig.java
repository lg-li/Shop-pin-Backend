package cn.edu.neu.shop.pin.websocket;

import cn.edu.neu.shop.pin.util.PinConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @ClassName WebSocketConfig
 * @Description TODO 配置WebSocket
 * @author flyhero
 */

@Configuration
@EnableWebSocketMessageBroker
// 注解开启STOMP协议来传输基于代理（message broker）的消息，这是控制器支持使用@MessageMaping，就像使用@RequestMapping一样
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        //注册一个Stomp的节点（endpoint）,并指定使用SockJS协议。
        stompEndpointRegistry.addEndpoint(PinConstants.WEBSOCKET_PATH).withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //服务端发送消息给客户端的域,多个用逗号隔开
        registry.enableSimpleBroker(PinConstants.WEBSOCKET_BROADCAST_PATH, PinConstants.P2P_PUSH_BASE_PATH);
        //定义一对一推送的时候前缀
        registry.setUserDestinationPrefix(PinConstants.P2P_PUSH_BASE_PATH);
        //定义websocket消息前缀
        registry.setApplicationDestinationPrefixes(PinConstants.WEBSOCKET_PATH_PREFIX);
    }
}
