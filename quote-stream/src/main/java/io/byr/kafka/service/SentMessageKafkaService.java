package io.byr.kafka.service;

import io.byr.kafka.utils.KafkaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Dean
 */
@Service
public class SentMessageKafkaService {
    @Autowired
    private KafkaUtil kafkaUtil;

    /**
     * 发送消息Kafka到消息队列
     */
    public List<Long> sentMessage(String topic, String messageInfo) throws ExecutionException, InterruptedException {
        try {
            kafkaUtil.sendSync(topic, messageInfo);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
