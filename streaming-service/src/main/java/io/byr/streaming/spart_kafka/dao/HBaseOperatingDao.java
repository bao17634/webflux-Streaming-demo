package io.byr.streaming.spart_kafka.dao;

import io.byr.streaming.spart_kafka.entity.StreamingWord;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName: HBaseOperatingService
 * @Description: TODO
 * @Author: yanrong
 * @Date: 2019/11/27 15:49
 */

public interface HBaseOperatingDao {
    /**
     * 创建表
     *
     * @param tableName
     * @param columnArray 列族名
     * @return
     */
     void createTable(String tableName, String[] columnArray) throws IOException;

    /**
     * 插入数据
     *
     * @param tableName
     * @param streamingWord
     * @return
     */
    void insertData(String tableName, StreamingWord streamingWord) throws IOException;

    /**
     * 获取表中所有数据
     *
     * @param tableName
     * @return
     */
    List<StreamingWord> getAllData(String tableName);

    /**
     * 获得每一行的数据
     *
     * @param tableName
     * @param rowKey
     * @return
     */
    StreamingWord getDataByRowKey(String tableName, String rowKey) throws IOException;

    /**
     * 获取表中原始数据
     *
     * @param tableName
     * @return
     */
    List getNoDealData(String tableName);

    /**
     * 获取指定但cell内容
     *
     * @param tableName
     * @param rowKey
     * @param family
     * @param col
     * @return
     */
    String getCellData(String tableName, String rowKey, String family, String col);

    /**
     * 删除指定cell数据
     *
     * @param tableName
     * @param rowKey
     * @return
     */
    void deleteByRowKey(String tableName, String rowKey) throws IOException;

    /**
     * 删除表
     *
     * @param tableName
     */
    void deleteTable(String tableName);

}
