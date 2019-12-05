package io.byr.streaming.spart_kafka.dao.impl;

import io.byr.streaming.spart_kafka.dao.StreamingDao;
import io.byr.streaming.spart_kafka.entity.StreamingWord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @ClassName: StreamingDao
 * @Description: TODO
 * @Author: yanrong
 * @Date: 2019/11/22 16:52
 */
@Repository("mysql")
public class StreamingDaoImpl implements StreamingDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public Integer saveStreaming(StreamingWord kafkaDTO) {
        String sql = "insert into Streaming_info(topic,value,count) values(?,?,?)";
        Integer a = jdbcTemplate.update(sql, new Object[]{kafkaDTO.getTopic(), kafkaDTO.getWord(), kafkaDTO.getCount()});
        return a;
    }

}
