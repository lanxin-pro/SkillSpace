package cn.iocoder.educate.rabbitmq.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/22 11:55
 */
public class Producer {

    public static void main(String[] args) {
        // 1.创建连接工程
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("35.79.223.30");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("dp899556677");
        connectionFactory.setVirtualHost("/"); // 发送到根节点

        Connection connection = null;
        Channel channel = null;
        try {
            // 2.创建连接
            connection = connectionFactory.newConnection("生产者");
            // 3.创建连接获取通道Channel
            channel = connection.createChannel();
            // 4.通过创建交换机，声明队列，绑定关系，路由key，发送消息，接受消息
            String queue = "queue1";
            /**
             * @params1 队列名称
             * @params2 是否要持久化durable-false 所谓持久化消息就是是否存盘
             * @params3 排他性 只有有一个消费者监听这个队列，当Connection关闭时，是否删除队列
             * @params4 是否自动删除 随着最后一个消费者消息完毕消息以后是否把队列自动删除
             * @params5 携带一些附加参数
             */
            channel.queueDeclare(queue,true,false,false,null);
            // 5.准备消息内容
            String message = "HelloLanXin！！！";
            // 6.发送消息给队列queue
            channel.basicPublish("",queue,null,message.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }finally {
            // 7.关闭通道
            if(channel != null && channel.isOpen()){
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
            // 8.关闭连接
            if(connection != null && connection.isOpen()){
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
