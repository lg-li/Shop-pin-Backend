package cn.edu.neu.shop.pin.websocket;

import cn.edu.neu.shop.pin.model.PinRole;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.service.security.UserService;
import cn.edu.neu.shop.pin.util.PinConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * @author flyhero
 * @ClassName WebSocketConfig
 * @Description TODO 配置WebSocket
 */

@Configuration
@EnableWebSocketMessageBroker
// 注解开启STOMP协议来传输基于代理（message broker）的消息，这是控制器支持使用@MessageMaping，就像使用@RequestMapping一样
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    @Autowired
    private UserService userService;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(PinConstants.WEBSOCKET_PATH).addInterceptors(new HandshakeInterceptor() {
            /**
             * websocket握手
             */
            @Override
            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                ServletServerHttpRequest req = (ServletServerHttpRequest) request;
                //获取token认证
                String src = req.getServletRequest().getParameter("src");
                String token = req.getServletRequest().getParameter("token");
                String oii = req.getServletRequest().getParameter("orderIndividualId");
                String ogi = req.getServletRequest().getParameter("orderGroupId");
                Integer orderIndividualId = Integer.parseInt(oii != null ? oii : "-1");
                Integer orderGroupId = Integer.parseInt(ogi != null ? ogi : "-1");
                //解析token获取用户信息
                Principal user = parseTokenToPrinciple(token, orderIndividualId, orderGroupId);
                if (user == null) { //如果token认证失败user为null，返回false拒绝握手
                    System.out.println("失败连接！！！！");
                    return false;
                }
                //保存认证用户
                System.out.println("连接成功！" + user.toString());
                attributes.put("user", user);
                return true;
            }

            @Override
            public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
                System.out.println("握手结束");
            }

        }).setHandshakeHandler(new DefaultHandshakeHandler() {
            @Override
            protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
                //设置认证用户
                return (Principal) attributes.get("user");
            }
        }).setAllowedOrigins("*");
    }

    private Principal parseTokenToPrinciple(String token, Integer orderIndividualId, Integer orderGroupId) {
        PinUser user = userService.whoDoesThisTokenBelongsTo(token);
        List<PinRole> roles = user.getRoles();
        for(PinRole role : roles) {
            if(role.equals(PinRole.ROLE_MERCHANT)) {
                return new CustomerPrinciple(user.getId(), orderIndividualId, orderGroupId);
            } else if(role.equals(PinRole.ROLE_USER)) {

            } else if(role.equals(PinRole.ROLE_ADMIN)) {

            } else {
                return null;
            }
        }
        return null;
    }

//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
//        //注册一个Stomp的节点（endpoint）,并指定使用SockJS协议。
//        stompEndpointRegistry.addEndpoint(PinConstants.WEBSOCKET_PATH).withSockJS();
//    }

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
