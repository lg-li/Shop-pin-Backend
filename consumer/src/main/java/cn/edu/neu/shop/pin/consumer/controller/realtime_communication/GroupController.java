package cn.edu.neu.shop.pin.consumer.controller.realtime_communication;

import cn.edu.neu.shop.pin.consumer.service.commons.UserAddressControllerService;
import cn.edu.neu.shop.pin.consumer.service.realtime_communication.GroupControllerService;
import cn.edu.neu.shop.pin.consumer.websocket.CustomerPrincipal;
import feign.Client;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@Import(FeignClientsConfiguration.class)
@RestController
@MessageMapping("/customer")
public class GroupController {
    private GroupControllerService groupControllerService;

    @Autowired
    public GroupController(
            Decoder decoder, Encoder encoder, Client client) {
        this.groupControllerService = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .target(GroupControllerService.class, "http://Pin-Provider");
    }

    @MessageMapping("/hello")
    public void hello(CustomerPrincipal customerPrincipal) {
        groupControllerService.hello(customerPrincipal);
    }
}
