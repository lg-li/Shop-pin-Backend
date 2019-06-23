package cn.edu.neu.shop.pin.message_queue.consumer;

import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.websocket.StompMessageService;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户消息队列消耗者
 */
@Component
@RabbitListener(queues = PinConstants.MessageQueueKey.USER)
public class UserMessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(UserMessageConsumer.class);

    private final StompMessageService stompMessageService;

    @Autowired
    public UserMessageConsumer(StompMessageService stompMessageService) {
        this.stompMessageService = stompMessageService;
    }

    /**
     * 消息消费方法为接受到消息后的处理方法
     */
    @RabbitHandler
    public void onReceive(String message) {
        logger.info("[Received USER]: " + message);
        JSONObject groupMessage = JSONObject.parseObject(message);
        stompMessageService.onReceiveUserMessageToRoute(
                groupMessage.getString("userId"),
                groupMessage.getString("route"),
                groupMessage.getJSONObject("data")
        );
    }

}