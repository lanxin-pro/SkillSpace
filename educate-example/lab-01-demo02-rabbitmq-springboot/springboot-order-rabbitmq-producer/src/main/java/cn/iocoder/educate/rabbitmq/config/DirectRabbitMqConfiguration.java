package cn.iocoder.educate.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/24 20:42
 */
@Configuration
public class DirectRabbitMqConfiguration {

    @Bean
    public DirectExchange directExchangeDirect(){
        return new DirectExchange("direct_order_exchange",true,false);
    }

    @Bean
    public Queue emailQueueDirect(){
        return new Queue("email.direct.queue", true);
    }

    @Bean
    public Queue smsQueueDirect(){
        return new Queue("sms.direct.queue",true);
    }

    @Bean
    public Queue duanxinQueueDirect(){
        return new Queue("duanxin.direct.queue",true);
    }

    /**
     * 绑定交换机
     * @return
     */
    @Bean
    public Binding smsBingDirect(){
        return BindingBuilder.bind(smsQueueDirect()).to(directExchangeDirect()).with("sms");
    }

    @Bean
    public Binding emailBingDirect(){
        return BindingBuilder.bind(emailQueueDirect()).to(directExchangeDirect()).with("email");
    }

    @Bean
    public Binding duanxinBingDirect(){
        return BindingBuilder.bind(duanxinQueueDirect()).to(directExchangeDirect()).with("duanxin");
    }

}
