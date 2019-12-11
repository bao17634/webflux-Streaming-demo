package io.byr.kafka.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @ClassName: kafkaConfig
 * @Description: TODO
 * @Author: yanrong
 * @Date: 2019/12/11 14:06
 */
@Configuration
public class kafkaConfig {

    @Bean
    public KafkaProducer<String, String> kafkaProducer() {
        Properties producerProps = new Properties();
        //必需的3个参数
        producerProps.put("bootstrap.servers", "localhost:9092");
        producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("acks", "all");
        producerProps.put("retries", 0);
        producerProps.put("batch.size", 16384);
        producerProps.put("linger.ms", 1);
        producerProps.put("buffer.memory", 33554432);
        KafkaProducer<String, String> producer = new KafkaProducer<>(producerProps);
        return producer;
    }
}
