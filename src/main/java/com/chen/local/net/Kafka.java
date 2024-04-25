package com.chen.local.net;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * TODO
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2024/3/29
 */
public class Kafka {

    private static final String SERVICE = "127.0.0.1:9092";
    private static final String TOPIC = "device-access";

    public static void main(String[] args) throws Exception {
        System.out.println(Kafka.send(TOPIC, "device_registerCode"));
    }

    public static Object send(String topic, String message) throws ExecutionException, InterruptedException {
        // 1. 创建 kafka 生产者的配置对象
        Properties properties = new Properties();
        // 2. 给 kafka 配置对象添加配置信息
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, SERVICE);
        // key,value 序列化（必须）：key.serializer，value.serializer
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 3. 创建 kafka 生产者对象
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
        // 4. 调用 send 方法,发送消息
        // 异步发送 默认
//        kafkaProducer.send(new ProducerRecord<>("first","kafka" + i));
        // 同步发送
        RecordMetadata result = kafkaProducer.send(new ProducerRecord<>(topic, message)).get();
        System.out.println("\n----------------");
        System.out.println(result);
        System.out.println("----------------\n");
        // 5. 关闭资源
        kafkaProducer.close();
        return result;
    }

}


