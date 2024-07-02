package cn.iocoder.educate.rabbitmq.service.topic;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Service;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/24 21:31
 */
@Service
@RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "email.topic.queue",durable = "true",autoDelete = "false"),
        exchange = @Exchange(value = "topic_order_exchange",type = ExchangeTypes.TOPIC),
        key = "*.email.#"
))
public class EmailConsumberTopic {

    @RabbitHandler
    public void reviceMessage(String message){
        System.out.println("email topic--接收到了订单消息->" + message);
    }
}
