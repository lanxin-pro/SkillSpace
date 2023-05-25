package cn.iocoder.boot.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/24 20:42
 */
@Configuration
public class DeadRabbitMqConfiguration {

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("dead_direct_exchange",true,false);
    }

    @Bean
    public Queue deadQueue(){
        return new Queue("dead.direct.queue",true,false,false);
    }

    @Bean
    public Binding deadBings(){
        return BindingBuilder.bind(deadQueue()).to(directExchange()).with("dead");
    }

}
