package cn.edu.neu.shop.pin.consumer.service.realtime_communication;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;


interface WebSocketMessageStream {
    final String INPUT = "webSocketMessageIn";
    final String OUTPUT = "webSocketMessageOut";

    /**
     * 输入
     */
    @Input(INPUT)
    SubscribableChannel input();

    /**
     * 输出
     */
    @Output(OUTPUT)
    MessageChannel output();
}