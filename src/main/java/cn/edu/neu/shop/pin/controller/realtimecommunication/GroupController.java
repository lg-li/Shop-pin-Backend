package cn.edu.neu.shop.pin.controller.realtimecommunication;

import cn.edu.neu.shop.pin.service.OrderGroupService;
import cn.edu.neu.shop.pin.websocket.CustomerPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@MessageMapping("/customer")
public class GroupController {

    private static Logger logger = LoggerFactory.getLogger(GroupController.class);

    private final OrderGroupService orderGroupService;

    public GroupController(OrderGroupService orderGroupService) {
        this.orderGroupService = orderGroupService;
    }

    @MessageMapping("/hello")
    public void hello(CustomerPrincipal customerPrincipal) {
        logger.info("Got Hello from user #" + customerPrincipal.getUserId());
        orderGroupService.sendGroupInitMessageToSingle(customerPrincipal);
    }
}
