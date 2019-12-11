package io.byr.streaming.spart_kafka.dao;

import io.byr.streaming.spart_kafka.entity.StreamingWord;
import org.apache.kudu.client.KuduException;
import org.apache.kudu.client.KuduTable;
import org.apache.kudu.client.OperationResponse;

import java.util.List;

public interface KuduOperatingDao {
     KuduTable createTable(String tableName) throws KuduException;
    OperationResponse insertData (String tableName, StreamingWord streamingWord) throws KuduException;
    OperationResponse deleteDataByKey(String tableName,String key) throws KuduException;
    OperationResponse updateDataByKey(String tableName,String key) throws KuduException;
    List<String> listData(String tableName, List<String> listColumns) throws KuduException;
}
