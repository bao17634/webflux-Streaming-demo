package io.byr.streaming.spart_kafka.server.impl;


import io.byr.streaming.spart_kafka.config.BeanFactoryHelper;
import io.byr.streaming.spart_kafka.dao.impl.HBaseOperatingDaoImpl;
import io.byr.streaming.spart_kafka.dao.impl.KuduOperatingDaoImpl;
import io.byr.streaming.spart_kafka.dao.impl.StreamingDaoImpl;
import io.byr.streaming.spart_kafka.entity.StreamingWord;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.util.*;
import java.util.regex.Pattern;

//import com.streaming.dao.impl.HBaseOperatingDaoImpl;


/**
 * @ClassName: JavaDirectKafkaDataService
 * @Description: TODO
 * @Author: yanrong
 * @Date: 2019/11/21 18:17
 */


/**
 * 使用Kafka中来自一个或多个主题的消息并进行字数统计。
 * 用法：JavaDirectKafkaDataService <经纪人> <groupId> <topics>
 * <brokers>是一个或多个Kafka经纪人的列表
 * <groupId>是要从主题中消费的消费者组名称
 * <topics>是要从中使用的一个或多个kafka主题的列表
 * <p>
 * 示例：
 * $ bin / run-example stream.JavaDirectKafkaDataService broker1-host：port，broker2-host：port
 * 消费者组topic1，topic2
 */
@Slf4j
@Service
public final class KafkaDataServiceImpl {
    private  String TOPIC;
    private static final Pattern SPACE = Pattern.compile("\"[A-Za-z]+\"");


    public void readKafkaData(String brokers, String groupId, String topics) throws Exception {
        // Create context with a 2 seconds batch interval
        TOPIC = topics;
        SparkConf sparkConf = new SparkConf().setMaster("local[*]").setAppName("JavaDirectKafkaDataService");
        JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Durations.seconds(2));

        Set<String> topicsSet = new HashSet<>(Arrays.asList(topics.split(",")));
        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        kafkaParams.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        kafkaParams.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        kafkaParams.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // Create direct kafka stream with brokers and topics
        JavaInputDStream<ConsumerRecord<String, String>> messages = KafkaUtils.createDirectStream(
                jssc,
                LocationStrategies.PreferConsistent(),
                ConsumerStrategies.Subscribe(topicsSet, kafkaParams));
        readStreaming(messages);
        jssc.start();
        jssc.awaitTermination();

    }

    private void readStreaming(JavaInputDStream<ConsumerRecord<String, String>> messages) {
        // 返回一个新的DStream
        JavaDStream<String> lines = messages.map(ConsumerRecord::value);
        //通过将函数应用于此DStream的所有元素，然后展平结果，来返回新的DStream
        JavaDStream<String> words = lines.flatMap(x ->
                Arrays.asList(SPACE.split(x)).iterator()
        );
        JavaPairDStream<String, Integer> wordCounts = words.mapToPair(s ->
                new Tuple2<>(s, 1)
        ).reduceByKey((i1, i2) -> i1 + i2);
        List<String> list = new ArrayList<String>();
        wordCounts.foreachRDD(rdd -> {
//             要是换成别的，就是换这里的连接操作
            rdd.foreachPartition(partition -> {
                StreamingWord streamingWord =new StreamingWord();
                HBaseOperatingDaoImpl hbase= BeanFactoryHelper.getTargetBean("HBase",HBaseOperatingDaoImpl.class);
                KuduOperatingDaoImpl kudu= BeanFactoryHelper.getTargetBean("Kudu",KuduOperatingDaoImpl.class);
                while (partition.hasNext()) {
                    try {
                        Thread.sleep(5000);
                        Tuple2<String, Integer> next = partition.next();
                        streamingWord.setId(UUID.randomUUID().toString().replace("-",""));
                        streamingWord.setTopic("spark");
                        streamingWord.setWord(next._1);
                        streamingWord.setCount(next._2);
                        kudu.insertData("streamingTest",streamingWord);
//                        hbase.insertData("streamingTest", streamingWord);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        });
        wordCounts.print();
    }
}

