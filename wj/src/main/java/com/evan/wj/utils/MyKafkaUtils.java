package com.evan.wj.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyKafkaUtils {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Value("${spring.kafka.topic}")     //引入application.properties中的数据
    private String topics;

    /**
     * producer发送数据
     * @param message
     */
    public void producerSend(String message) {
        kafkaTemplate.send(topics, message);
        log.info("发送的消息：{}", message);
    }

    /**
     * kafka的consumer接收消息
     * @param record
     */
    @KafkaListener(topics = "${spring.kafka.topic}")
    public void consumerReceive(ConsumerRecord record) {
        log.info("接收到的数据：topic={},message={}", record.topic(), record.value());
    }
}
