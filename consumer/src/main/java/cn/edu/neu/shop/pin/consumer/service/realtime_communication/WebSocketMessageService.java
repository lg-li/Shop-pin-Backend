package cn.edu.neu.shop.pin.consumer.service.realtime_communication;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(WebSocketMessageStream.class)
public class WebSocketMessageService {

    @Autowired
    private WebSocketMessageStream stream;
    @Autowired
    private AmqpTemplate amqpTemplate;

    @StreamListener(WebSocketMessageStream.INPUT)
    void messageReceived(String routeKey, Object message) {
        try {
            amqpTemplate.convertAndSend(routeKey,message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    void send(String destination, Object body) {
        stream.output().send(MessageBuilder.withPayload(body).setReplyChannelName(destination).build());
    }
}
