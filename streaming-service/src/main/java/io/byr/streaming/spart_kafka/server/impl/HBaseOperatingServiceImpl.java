package io.byr.streaming.spart_kafka.server.impl;


import io.byr.streaming.spart_kafka.dao.HBaseOperatingDao;
import io.byr.streaming.spart_kafka.entity.StreamingWord;
import io.byr.streaming.spart_kafka.server.HBaseOperatingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName: HBaseOperatingServiceImpl
 * @Description: TODO
 * @Author: yanrong
 * @Date: 2019/11/28 15:14
 */
@Slf4j
@Service
public class HBaseOperatingServiceImpl implements HBaseOperatingService {
    @Autowired
    HBaseOperatingDao hBaseOperatingDao;
    @Override
    public void createTable(String tableName, String[] columnArray) throws IOException {
        hBaseOperatingDao.createTable(tableName,columnArray);
    }

    @Override
    public void insertData(String tableName, StreamingWord streamingWord) throws IOException {
        hBaseOperatingDao.insertData(tableName, streamingWord);
    }

    @Override
    public List<StreamingWord> getAllData(String tableName) {
        return hBaseOperatingDao.getAllData(tableName);
    }

    @Override
    public StreamingWord getDataByRowKey(String tableName, String rowKey) throws IOException {
        return hBaseOperatingDao.getDataByRowKey(tableName,rowKey);
    }

    @Override
    public List getNoDealData(String tableName) {
        return hBaseOperatingDao.getNoDealData(tableName);
    }

    @Override
    public String getCellData(String tableName, String rowKey, String family, String col) {
        return hBaseOperatingDao.getCellData(tableName,rowKey,family,col);
    }

    @Override
    public void deleteByRowKey(String tableName, String rowKey) throws IOException {
    hBaseOperatingDao.deleteByRowKey(tableName,rowKey);
    }

    @Override
    public void deleteTable(String tableName) {
        hBaseOperatingDao.deleteTable(tableName);
    }
}
