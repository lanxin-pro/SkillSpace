package cn.iocoder.educate.rabbitmq.service;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/24 20:37
 */
@Service
public class OrderService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void makeOrder(String userId,String productId,int num){
        // 1：根据商品id查询库存

        // 2：保存订单

        // 3：通过MQ来完成消息的分发
        /**
         * @Parpam1 交换机
         * @Parpam2 路由key
         * @Parpam3 消息内容
         */
        String exchangeName = "fanout_order_exchange";
        String routingKey = "";
        System.out.println("订单生成成功" + productId);
        rabbitTemplate.convertAndSend(exchangeName,routingKey,productId);
    }

    public void makeOrderDirect(String userId,String productId,int num){
        // 1：根据商品id查询库存

        // 2：保存订单

        // 3：通过MQ来完成消息的分发
        /**
         * @Parpam1 交换机
         * @Parpam2 路由key
         * @Parpam3 消息内容
         */
        String exchangeName = "direct_order_exchange";
        System.out.println("订单生成成功" + productId);
        rabbitTemplate.convertAndSend(exchangeName,"email",productId);
        rabbitTemplate.convertAndSend(exchangeName,"duanxin",productId);
    }

    public void makeOrderTopic(String userId,String productId,int num){
        // 1：根据商品id查询库存

        // 2：保存订单

        // 3：通过MQ来完成消息的分发
        /**
         * @Parpam1 交换机
         * @Parpam2 路由key
         * @Parpam3 消息内容
         */
        String exchangeName = "topic_order_exchange";
        System.out.println("订单生成成功" + productId);
        String routingKey = "com.email.duanxin";
        rabbitTemplate.convertAndSend(exchangeName,routingKey,productId);
    }

    public void makeOrderTtl(String userId,String productId,int num){
        // 1：根据商品id查询库存

        // 2：保存订单

        // 3：通过MQ来完成消息的分发
        /**
         * @Parpam1 交换机
         * @Parpam2 路由key
         * @Parpam3 消息内容
         */
        String exchangeName = "ttl_direct_order_exchange";
        System.out.println("订单生成成功" + productId);
        String routingKey = "ttl";
        rabbitTemplate.convertAndSend(exchangeName,routingKey,productId);
    }

    public void makeOrderTtlMessage(String userId,String productId,int num){
        // 1：根据商品id查询库存

        // 2：保存订单

        // 3：通过MQ来完成消息的分发
        /**
         * @Parpam1 交换机
         * @Parpam2 路由key
         * @Parpam3 消息内容
         */
        String exchangeName = "ttl_direct_order_exchange";
        System.out.println("订单生成成功" + productId);
        String routingKey = "ttlMessage";
        // 设置过期时间
        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration("5000");
                message.getMessageProperties().setContentEncoding("UTF-8");
                return message;
            }
        };
        rabbitTemplate.convertAndSend(exchangeName,routingKey,productId,messagePostProcessor);
    }
}
