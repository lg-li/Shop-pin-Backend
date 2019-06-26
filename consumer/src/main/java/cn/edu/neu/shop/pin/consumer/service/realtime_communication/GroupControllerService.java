package cn.edu.neu.shop.pin.consumer.service.realtime_communication;

import cn.edu.neu.shop.pin.consumer.websocket.CustomerPrincipal;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Service
public interface GroupControllerService {
    @MessageMapping("/customer/hello")
    public void hello(CustomerPrincipal customerPrincipal);
}
