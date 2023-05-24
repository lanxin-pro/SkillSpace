package cn.iocoder.educate.rabbitmq.all;

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
            // 5.准备消息内容
            String message = "HelloLanXin！！！";
            // 6.准备交换机
            String exchanageName = "direct_message_exchange";
            // 自定义类型
            String exchanageType = "direct";

            // 持久化，交换机不会随着服务器重启造成丢失
            channel.exchangeDeclare(exchanageName,exchanageType,true);
            channel.queueDeclare("queue5",true,false,false,null);
            channel.queueDeclare("queue6",true,false,false,null);
            channel.queueDeclare("queue7",true,false,false,null);
            // 绑定队列
            channel.queueBind("queue5",exchanageName,"order");
            channel.queueBind("queue6",exchanageName,"order");
            channel.queueBind("queue7",exchanageName,"course");

            // 自定义路由
            String routeKey = "course";
            channel.basicPublish(exchanageName,routeKey,null,message.getBytes());

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
