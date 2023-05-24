package cn.iocoder.educate.rabbitmq.work.lunxun;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/22 11:56
 */
public class Work2 {

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
                // 3: 从连接工厂中获取连接
                connection = connectionFactory.newConnection("消费者-Work1");
                // 4: 从连接中获取通道channel
                channel = connection.createChannel();

                // 5: 申明队列queue存储消息
                /*
                 *  如果队列不存在，则会创建
                 *  Rabbitmq不允许创建两个相同的队列名称，否则会报错。
                 *
                 *  @params1： queue 队列的名称
                 *  @params2： durable 队列是否持久化
                 *  @params3： exclusive 是否排他，即是否私有的，如果为true,会对当前队列加锁，其他的通道不能访问，并且连接自动关闭
                 *  @params4： autoDelete 是否自动删除，当最后一个消费者断开连接之后是否自动删除消息。
                 *  @params5： arguments 可以设置队列附加参数，设置队列的有效期，消息的最大长度，队列的消息生命周期等等。
                 * */
                // 这里如果queue已经被创建过一次了，可以不需要定义
//            channel.queueDeclare("queue1", false, false, false, null);
                // 同一时刻，服务器只会推送一条消息给消费者
                // 6： 定义接受消息的回调

                channel.basicQos(1);
                // 自动应答是轮询，手动应答是公平
                channel.basicConsume(queueName, true, new DeliverCallback() {
                    @Override
                    public void handle(String s, Delivery delivery) throws IOException {
                        System.out.println("Work2----收到消息是：" + new String(delivery.getBody(), "UTF-8"));
                    }
                }, new CancelCallback() {
                    @Override
                    public void handle(String s) throws IOException {
                        System.out.println("Work2----接受失败了");
                    }
                });
                System.in.read();
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
        new Thread(runnable,"queue6").start();
        new Thread(runnable,"queue7").start();
    }
}
