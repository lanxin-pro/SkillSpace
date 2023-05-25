package cn.iocoder.boot.rabbitmq.service.direct;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/24 21:31
 */
@Service
@RabbitListener(queues = {"sms.direct.queue"})
public class SmsConsumberDirect {

    @RabbitHandler
    public void reviceMessage(String message){
        System.out.println("sms direct--接收到了订单消息->" + message);
    }
}
