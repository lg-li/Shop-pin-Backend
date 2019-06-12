package cn.edu.neu.shop.pin.websocket;

import cn.edu.neu.shop.pin.mapper.PinOrderIndividualMapper;
import cn.edu.neu.shop.pin.model.PinOrderGroup;
import cn.edu.neu.shop.pin.model.PinOrderIndividual;
import cn.edu.neu.shop.pin.model.PinRole;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.service.OrderGroupService;
import cn.edu.neu.shop.pin.service.security.UserService;
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
import java.util.Objects;

@Configuration
@EnableWebSocketMessageBroker
// 注解开启STOMP协议来传输基于代理（message broker）的消息，这是控制器支持使用@MessageMaping，就像使用@RequestMapping一样
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final UserService userService;

    private final OrderGroupService orderGroupService;

    private final PinOrderIndividualMapper pinOrderIndividualMapper;

    public WebSocketConfig(UserService userService, OrderGroupService orderGroupService, PinOrderIndividualMapper pinOrderIndividualMapper) {
        this.userService = userService;
        this.orderGroupService = orderGroupService;
        this.pinOrderIndividualMapper = pinOrderIndividualMapper;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/pin-websocket").addInterceptors(new HandshakeInterceptor() {
            /**
             * websocket握手
             */
            @Override
            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
                ServletServerHttpRequest req = (ServletServerHttpRequest) request;
                //获取token认证
                String src = req.getServletRequest().getParameter("src");
                String token = req.getServletRequest().getParameter("token");
                String ogi = req.getServletRequest().getParameter("orderGroupId");
                String si = req.getServletRequest().getParameter("storeId");
                Integer orderGroupId = Integer.parseInt(ogi != null && !ogi.equals("undefined") ? ogi : "-1");
                Integer storeId = Integer.parseInt(si != null && !si.equals("undefined") ? si : "-1");
                //解析token获取用户信息
                Principal user = parseTokenToPrinciple(src, token, orderGroupId, storeId);
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

    /**
     * @param src 用户来源是商户还是买家
     * @param token 验证用户身份
     * @param orderGroupId 团单ID
     * @param storeId 店铺ID
     * @return 解析出来的Principal变量
     * @author flyhero
     * 根据src（source的缩写）判断用户来意（用途），根据token来验证授权
     */
    private Principal parseTokenToPrinciple(String src, String token, Integer orderGroupId, Integer storeId) {
        PinUser user = userService.whoDoesThisTokenBelongsTo(token);
        List<PinRole> roles = user.getRoles();
        if (Objects.equals(src, "customer")) {
            for (PinRole role : roles) {
                if (role.equals(PinRole.ROLE_USER)) {
                    PinOrderGroup orderGroup = orderGroupService.findById(orderGroupId);
                    if (orderGroup == null) {
                        return null;
                    }
                    PinOrderIndividual orderIndividualSample = new PinOrderIndividual();
                    orderIndividualSample.setUserId(user.getId());
                    orderIndividualSample.setOrderGroupId(orderGroupId);
                    PinOrderIndividual orderIndividual = pinOrderIndividualMapper.selectOne(orderIndividualSample);
                    if (orderIndividual == null) {
                        return null;
                    }
                    return new CustomerPrincipal(user.getId(), orderIndividual.getId(), orderGroupId);
                }
            }
            return null;
        } else if (Objects.equals(src, "merchant")) {
            for (PinRole role : roles) {
                if (role.equals(PinRole.ROLE_MERCHANT)) {
                    return new MerchantPrincipal(user.getId(), storeId);
                }
            }
            return null;
        } else if (Objects.equals(src, "admin")) {
            for (PinRole role : roles) {
                if (role.equals(PinRole.ROLE_ADMIN)) {
                    return new AdminPrincipal(user.getId());
                }
            }
            return null;
        } else {
            return null;
        }
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //服务端发送消息给客户端的域,多个用逗号隔开
        registry.enableSimpleBroker("/group", "/user", "/merchant");
        //定义一对一推送的时候前缀
        registry.setUserDestinationPrefix("/user");
        //定义websocket消息前缀
        registry.setApplicationDestinationPrefixes("/server");
    }
}
