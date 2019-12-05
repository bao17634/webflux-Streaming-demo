package io.byr.kafka.controller;


import io.byr.kafka.service.SentMessageKafkaService;
import io.byr.kafka.utils.KafkaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * @author ：Byr
 * @date ：Created in 2019/9/21 22:30
 * @description：
 * @modified By：
 * @modified Date：
 * @version: $
 */
//@RequestMapping(value = "/")
@RestController
@Slf4j
public class ProduceController {
    @Autowired
    private SentMessageKafkaService sentMessageKafkaService;

    @RequestMapping(value = "/sentMessage")
    public void sentMessage() throws ExecutionException, InterruptedException {
        try {
            for (int i = 0; i < 1000; i++) {
                Thread.sleep(1000);
                String uuid = UUID.randomUUID().toString().replace("-",",");
                log.info("随机字符串：{}",uuid);
                sentMessageKafkaService.sentMessage("spark", uuid);
            }
            KafkaUtil.getProducer().flush();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
