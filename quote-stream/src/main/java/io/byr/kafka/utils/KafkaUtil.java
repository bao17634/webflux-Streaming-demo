package io.byr.kafka.utils;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @author Dean
 */
@Component
public class KafkaUtil {
	@Autowired
	private KafkaProducer producer;


	/**
	 * 同步发送消息
	 *
	 * @param topic topic
	 * @param value 消息内容
	 */
	public  void sendSync(String topic, String value) throws ExecutionException, InterruptedException {
		producer.send(new ProducerRecord<>(topic, value)).get();
	}
	public  KafkaProducer<String, String> getProducer(){
		return producer;
	}
}
