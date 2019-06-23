package cn.edu.neu.shop.pin.message_queue.producer;

import cn.edu.neu.shop.pin.util.PinConstants;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageOnQueueProducer {

    private static Logger logger = LoggerFactory.getLogger(MessageOnQueueProducer.class);

    private final AmqpTemplate rabbitTemplate;

    @Autowired
    public MessageOnQueueProducer(AmqpTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendToGroup(String route, JSONObject data) {
        JSONObject jsonToSend = new JSONObject();
        jsonToSend.put("route", route);
        jsonToSend.put("data", data);
        logger.info("[Sending to GROUP] send msg:" + jsonToSend);
        this.rabbitTemplate.convertAndSend(PinConstants.MessageQueueKey.GROUP, jsonToSend.toJSONString());
    }

    public void sendToUser(String userId, String route, JSONObject data) {
        JSONObject jsonToSend = new JSONObject();
        jsonToSend.put("userId", userId);
        jsonToSend.put("route", route);
        jsonToSend.put("data", data);
        logger.info("[Sending to USER] send msg:" + jsonToSend);
        this.rabbitTemplate.convertAndSend(PinConstants.MessageQueueKey.USER, jsonToSend.toJSONString());
    }

}