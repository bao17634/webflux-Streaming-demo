package io.byr.streaming.spart_kafka.dao;

import io.byr.streaming.spart_kafka.entity.StreamingWord;

public interface StreamingDao {
    Integer saveStreaming(StreamingWord kafkaDTO);
}
