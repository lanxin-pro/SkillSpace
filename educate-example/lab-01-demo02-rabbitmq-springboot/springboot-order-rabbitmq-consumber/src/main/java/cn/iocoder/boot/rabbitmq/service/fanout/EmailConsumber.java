package cn.iocoder.boot.rabbitmq.service.fanout;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/24 21:31
 */
@Service
@RabbitListener(queues = {"email.fanout.queue"})
public class EmailConsumber {

    @RabbitHandler
    public void reviceMessage(String message){
        System.out.println("email fanout--接收到了订单消息->" + message);
    }
}
