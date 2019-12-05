package io.byr.streaming.spart_kafka.controller;

import io.byr.streaming.spart_kafka.dao.HBaseOperatingDao;
import io.byr.streaming.spart_kafka.entity.StreamingWord;
import io.byr.streaming.spart_kafka.server.impl.KafkaDataServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author ：Byr
 * @date ：Created in 2019/9/21 22:30
 * @description：
 * @modified By：
 * @modified Date：
 * @version: $
 */

@RestController
@Slf4j
@RequestMapping(value = "/spark")
public class SparkController {
    @Autowired
    private KafkaDataServiceImpl kafkaDataServiceImpl;
    @Autowired
    private HBaseOperatingDao hBaseOperatingDao;
    @RequestMapping(value = "/test")
    public String StreamingInfo() throws Exception {
        String brokers = "localhost:9092";
        String groupId = "VoucherGroup";
        String topics = "spark";
        try {
            kafkaDataServiceImpl.readKafkaData(brokers, groupId, topics);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "receive";
    }
    @RequestMapping(value = "/queryHBaseData")
    public List<StreamingWord> queryHBaseData(){
        String tableName="streamingTest1";
        List<StreamingWord> list=hBaseOperatingDao.getAllData(tableName);
        return list;
    }

}
