package io.byr.streaming.spart_kafka.entity;

import lombok.Data;

/**
 * @ClassName: StreamingWord
 * @Description: TODO
 * @Author: yanrong
 * @Date: 2019/11/27 16:17
 */
@Data
public class StreamingWord {
    private String id;
    private String word;
    private Integer count;
    private String topic;

    @Override
    public String toString() {
        return "StreamingWord{" +
                "id='" + id + '\'' +
                ", username='" + word + '\'' +
                ", password='" + count + '\'' +
                ", gender='" + topic + '\'' +
                '}';
    }
}
