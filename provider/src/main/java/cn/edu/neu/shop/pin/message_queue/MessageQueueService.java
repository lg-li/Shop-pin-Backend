package cn.edu.neu.shop.pin.message_queue;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageQueueService {

    @Autowired
    private
    RabbitTemplate rabbitTemplate;

    public void send(String router, String message) {
        rabbitTemplate.convertAndSend(router, message);
    }

}