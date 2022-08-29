package cn.itcast.mq.listener;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class SpringRabbitListener {

    // 简单队列： 一个生产者对应一个消费者
    //1. 手动创建，需在RabbitMQ中手动创建myQueue1 队列，否则报错
    @RabbitListener(queues = "simple.queue")
    public void listenBasicQueue(String msg) {
        System.out.println("消费者接收到simple.queue的消息：【" + msg + "】");
    }

    // 工作队列： 一个生产者对应多个消费者
    //2. 自动创建队列
    @RabbitListener(queuesToDeclare = @Queue("work.queue"))
    public void listenWorkQueue1(String msg) throws InterruptedException {
        System.out.println("消费者-1-接收到 work.queue的消息：【" + msg + "】" + LocalDateTime.now());
        Thread.sleep(20);
    }

    // 工作队列： 一个生产者对应多个消费者
    @RabbitListener(queuesToDeclare = @Queue("work.queue"))
    public void listenWorkQueue2(String msg) throws InterruptedException {
        System.out.println("消费者-2-接收到 work.queue的消息：【" + msg + "】" + LocalDateTime.now());
        Thread.sleep(200);
    }

    // 发布订阅--Fanout： 一个生产者对应多个消费者
    //2. 自动创建队列
    @RabbitListener(queues = "fanout.queue1")
    public void listenFanoutQueue1(String msg) throws InterruptedException {
        System.out.println("消费者-1-接收到 fanout.queue1 的消息：【" + msg + "】" + LocalDateTime.now());
        Thread.sleep(20);
    }

    // 发布订阅--Fanout： 一个生产者对应多个消费者
    @RabbitListener(queues = "fanout.queue2")
    public void listenFanoutQueue2(String msg) throws InterruptedException {
        System.out.println("消费者-2-接收到 fanout.queue2 的消息：【" + msg + "】" + LocalDateTime.now());
        Thread.sleep(200);
    }

    // 发布订阅--Direct： 一个生产者对应多个消费者
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue1"),
            exchange = @Exchange(name = "super.direct", type = ExchangeTypes.DIRECT),
            key = {"red", "blue"}))
    public void listenDirectQueue1(String msg) throws InterruptedException {
        System.out.println("消费者-1-接收到 direct.queue1 的消息：【" + msg + "】" + LocalDateTime.now());
        Thread.sleep(20);
    }

    // 发布订阅--Direct： 一个生产者对应多个消费者
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue2"),
            exchange = @Exchange(name = "super.direct"),
            key = {"red", "yellow"}))
    public void listenDirectQueue2(String msg) throws InterruptedException {
        System.out.println("消费者-2-接收到 direct.queue2 的消息：【" + msg + "】" + LocalDateTime.now());
        Thread.sleep(20);
    }

    // 发布订阅--Topic： 一个生产者对应多个消费者
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "topic.queue1"),
            exchange = @Exchange(name = "super.topic", type = ExchangeTypes.TOPIC),
            key = "china.#"))
    public void listenTopicQueue1(String msg) throws InterruptedException {
        System.out.println("消费者-1-接收到 topic.queue1 的消息：【" + msg + "】" + LocalDateTime.now());
        Thread.sleep(20);
    }

    // 发布订阅--Topic： 一个生产者对应多个消费者
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "topic.queue2"),
            exchange = @Exchange(name = "super.topic", type = ExchangeTypes.TOPIC),
            key = "*.news"))
    public void listenTopicQueue2(String msg) throws InterruptedException {
        System.out.println("消费者-2-接收到 topic.queue2 的消息：【" + msg + "】" + LocalDateTime.now());
        Thread.sleep(20);
    }

    @RabbitListener(queues = "object.queue")
    public void listenObjectQueue(Map<String, Object> map) {
        System.out.println("消费者接收到 object.queue2 的消息：【" + map + "】" + LocalDateTime.now());
    }
}
