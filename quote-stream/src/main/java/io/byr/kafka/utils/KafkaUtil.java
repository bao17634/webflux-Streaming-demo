package io.byr.kafka.utils;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @author Dean
 */
public class KafkaUtil {
	private static KafkaProducer<String, String> producer;


	static {
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
		producer = new KafkaProducer<>(producerProps);
	}

	/**
	 * 同步发送消息
	 *
	 * @param topic topic
	 * @param value 消息内容
	 */
	public static void sendSync(String topic, String value) throws ExecutionException, InterruptedException {
		producer.send(new ProducerRecord<>(topic, value)).get();
	}
	public static KafkaProducer<String, String> getProducer(){
		return producer;
	}
}
