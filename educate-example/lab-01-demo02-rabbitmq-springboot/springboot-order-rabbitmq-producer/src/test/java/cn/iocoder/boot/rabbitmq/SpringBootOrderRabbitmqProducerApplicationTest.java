package cn.iocoder.boot.rabbitmq;

import cn.iocoder.boot.rabbitmq.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/24 20:52
 */
@SpringBootTest
public class SpringBootOrderRabbitmqProducerApplicationTest {

    @Autowired
    private OrderService orderService;

    @Test
    void contextLoad(){
        orderService.makeOrder("1","1",12);
    }

    @Test
    void contextLoadDirect(){
        orderService.makeOrderDirect("1","1",12);
    }

    @Test
    void contextLoadTopic(){
        orderService.makeOrderTopic("1","1",12);
    }

    @Test
    void contextLoadTtl(){
        orderService.makeOrderTtl("1","1",12);
    }

    @Test
    void contextLoadTtlMessage(){
        orderService.makeOrderTtlMessage("1","1",12);
    }
}
