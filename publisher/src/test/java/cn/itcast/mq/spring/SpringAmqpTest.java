package cn.itcast.mq.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringAmqpTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 简单队列： 一个生产者对应一个消费者
    @Test
    public void testSendMessage2BasicQueue() {
        String queueName = "simple.queue";
        String message = "hello, spring amqp";
        rabbitTemplate.convertAndSend(queueName, message);
    }

    // 工作队列： 一个生产者对应多个消费者
    @Test
    public void testSendMessage2WorkQueue() throws InterruptedException {
        String queueName = "work.queue";
        String message = "hello, spring amqp__";
        for (int i = 0; i < 50; i++) {
            rabbitTemplate.convertAndSend(queueName, message + i);
            Thread.sleep(20);
        }
    }

    @Test
    public void testSendFanoutExchange() {
        // 交换机名称
        String exchangeName = "super.fanout";
        // 消息
        String message = "hello, every one";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "", message);
    }

    @Test
    public void testSendDirectExchange() {
        // 交换机名称
        String exchangeName = "super.direct";
        // 消息
        String message = "hello, every";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "blue", message + " blue");
        rabbitTemplate.convertAndSend(exchangeName, "red", message + " red");
        rabbitTemplate.convertAndSend(exchangeName, "yellow", message + " yellow");
    }

    @Test
    public void testSendTopicExchange() {
        // 交换机名称
        String exchangeName = "super.topic";
        // 消息
        String message = "hello,";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "china.news", message + " china.news");
        rabbitTemplate.convertAndSend(exchangeName, "japan.news", message + " japan.news");
    }

    // 用于测试 队列传递的数据是如何转换的
    @Test
    public void testSendMessage2Queue() {
        String queueName = "object.queue";
        Map<String, Object> map = new HashMap<>();
        map.put("name", "柳岩");
        map.put("age", 37);
        // 默认： content_type:	application/x-java-serialized-object
        // Spring对象由org.springframework.amqp.support.converter.MessageConverter 处理
        // 默认实现SimpleMessageConverter,基于JDK的ObjectOutputStream完成序列化。
        // 如果修改只需要定义一个MessageConverter类型的Bean即可。推荐JSON序列化
        // 在PublisherApplication中定义，可以学习
        rabbitTemplate.convertAndSend(queueName, map);
    }
}
