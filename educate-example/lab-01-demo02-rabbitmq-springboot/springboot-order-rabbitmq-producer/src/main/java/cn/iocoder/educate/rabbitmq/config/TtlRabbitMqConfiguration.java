package cn.iocoder.educate.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/24 20:42
 */
@Configuration
public class TtlRabbitMqConfiguration {

    @Bean
    public DirectExchange ttlDirectExchangeDirect(){
        return new DirectExchange("ttl_direct_order_exchange",true,false);
    }

    /**
     * 设置过期时间
     * @return
     */
    @Bean
    public Queue ttlQueueDirect(){
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("x-message-ttl",5000);
        stringObjectHashMap.put("x-dead-letter-exchange","dead_direct_exchange");
        stringObjectHashMap.put("x-dead-letter-routing-key","dead");
        stringObjectHashMap.put("x-max-length",2);
        return new Queue("ttl.direct.queue",true,false,false,stringObjectHashMap);
    }

    @Bean
    public Queue ttlMessageQueueDirect(){
        return new Queue("ttl.message.direct.queue",true);
    }

    @Bean
    public Binding ttlBingDirect(){
        return BindingBuilder.bind(ttlQueueDirect()).to(ttlDirectExchangeDirect()).with("ttl");
    }

    @Bean
    public Binding ttlMessageBingDirect(){
        return BindingBuilder.bind(ttlMessageQueueDirect()).to(ttlDirectExchangeDirect()).with("ttlMessage");
    }

}
