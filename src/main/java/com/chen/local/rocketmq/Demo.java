package com.chen.local.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.List;

/**
 * RocketMQ Demo
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/5/17
 */
public class Demo {

    private static final String NAMESERVER_ADDR = "127.0.0.1:9876";

    private static final String GROUP = "local_test_group";
    private static final String TOPIC = "local_test_topic";

    public void producer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer(GROUP);
        producer.setNamesrvAddr(NAMESERVER_ADDR);
        producer.start();

        for (int i = 0; i < 100; ++i) {
            String content = "Hello RocketMQ! index: " + i;
            Message msg = new Message(TOPIC, "tagA", content.getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult result = producer.send(msg);
            System.out.printf("SendResult status:%s,brokerName:%s, queueId:%d, queueOffset:%s%n"
                    , result.getSendStatus()
                    , result.getMessageQueue().getBrokerName()
                    , result.getMessageQueue().getQueueId()
                    , result.getQueueOffset());
        }

        producer.shutdown();
    }

    public void consumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(GROUP);
        consumer.setNamesrvAddr(NAMESERVER_ADDR);
        consumer.subscribe(TOPIC, "*");
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
            try {
                String msg = new String(msgs.get(0).getBody(), RemotingHelper.DEFAULT_CHARSET);
                System.out.printf("Receive Message: %s %n", msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
        System.out.printf("Consumer Started.%n");
    }

    public static void main(String[] args) throws Exception {
        Demo demo = new Demo();
        try {
            demo.consumer();
            System.out.println("=== consumer end ===");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Thread.sleep(1000L);

        try {
            demo.producer();
            System.out.println("=== producer end ===");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


