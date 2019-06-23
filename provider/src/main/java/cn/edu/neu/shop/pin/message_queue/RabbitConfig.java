package cn.edu.neu.shop.pin.message_queue;

import cn.edu.neu.shop.pin.util.PinConstants;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    /**
     * 定义 Group 队列
     * @return Group Queue
     */
    @Bean
    public Queue group() {
        return QueueBuilder.durable(PinConstants.MessageQueueKey.GROUP).build();
    }
    /**
     * 定义 User 队列
     * @return User Queue
     */
    @Bean
    public Queue user() {
        return QueueBuilder.durable(PinConstants.MessageQueueKey.USER).build();
    }
}