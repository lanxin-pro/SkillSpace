package cn.iocoder.educate.rabbitmq.routing;

import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/22 11:56
 */
public class Consumer {

    private static Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // 1.创建连接工程
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("35.79.223.30");
            connectionFactory.setPort(5672);
            connectionFactory.setUsername("admin");
            connectionFactory.setPassword("dp899556677");
            connectionFactory.setVirtualHost("/"); // 发送到根节点

            String queueName = Thread.currentThread().getName();
            Connection connection = null;
            Channel channel = null;
            try {
                // 2.创建连接
                connection = connectionFactory.newConnection("生产者");
                // 3.创建连接获取通道Channel
                channel = connection.createChannel();

                channel.basicConsume(queueName, true, new DeliverCallback() {
                    @Override
                    public void handle(String s, Delivery delivery) throws IOException {
                        System.out.println("收到消息是：" + new String(delivery.getBody(), "UTF-8"));
                    }
                }, new CancelCallback() {
                    @Override
                    public void handle(String s) throws IOException {
                        System.out.println("接受失败了");
                    }
                });
                System.out.println("开始接受消息了");

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
    };

    public static void main(String[] args) {
       new Thread(runnable,"queue1").start();
       new Thread(runnable,"queue2").start();
       new Thread(runnable,"queue3").start();
    }
}
