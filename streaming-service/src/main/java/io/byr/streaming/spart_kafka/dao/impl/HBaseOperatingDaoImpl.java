package io.byr.streaming.spart_kafka.dao.impl;

import io.byr.streaming.spart_kafka.dao.HBaseOperatingDao;
import io.byr.streaming.spart_kafka.entity.StreamingWord;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName: HBaseOperatingDaoImpl
 * @Description: TODO
 * @Author: yanrong
 * @Date: 2019/11/27 15:49
 */
@Slf4j
@Repository("HBase")
public class HBaseOperatingDaoImpl implements HBaseOperatingDao {
    private static Admin admin;
    @Autowired
    private Connection connection;
    @Override
    public void createTable(String tableName, String[] columnArray) throws IOException {
        TableName tableNameNew = TableName.valueOf(tableName);
        admin = connection.getAdmin();
        try {
            if (admin.tableExists(tableNameNew)) {
                log.warn("表已经存在！");
            } else {
                HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName);
                for (String col : columnArray) {
                    HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(col);
                    hTableDescriptor.addFamily(hColumnDescriptor);
                }
                admin.createTable(hTableDescriptor);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insertData(String tableName, StreamingWord streamingWord) throws IOException {
        createTable(tableName, new String[]{"StreamingWord"});
        TableName tablename = TableName.valueOf(tableName);
        String count=String.valueOf(streamingWord.getCount());
        try {
            Put put = new Put((streamingWord.getId()).getBytes());
            //参数：1.列族名  2.列名  3.值
            put.addColumn("StreamingWord".getBytes(), "word".getBytes(), streamingWord.getWord().getBytes());
            put.addColumn("StreamingWord".getBytes(), "count".getBytes(), count.getBytes());
            put.addColumn("StreamingWord".getBytes(), "topic".getBytes(), streamingWord.getTopic().getBytes());
            Table table = connection.getTable(tablename);
            table.put(put);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<StreamingWord> getAllData(String tableName) {
        Table table = null;
        List<StreamingWord> list = new ArrayList<StreamingWord>();
        try {
            table = connection.getTable(TableName.valueOf(tableName));
            ResultScanner results = table.getScanner(new Scan());
            StreamingWord streamingWord = null;
            for (Result result : results) {
                String id = new String(result.getRow());
                streamingWord = new StreamingWord();
                for (Cell cell : result.rawCells()) {
                    String row = Bytes.toString(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());
                    String colName = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
                    String value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                    streamingWord.setId(row);
                    if (colName.equals("word")) {
                        streamingWord.setWord(value);
                    }
                    if (colName.equals("count")) {
                        streamingWord.setCount(Integer.valueOf(value));
                    }
                    if (colName.equals("topic")) {
                        streamingWord.setTopic(value);
                    }
                }
                list.add(streamingWord);
            }
        } catch (IOException e) {
           throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public StreamingWord getDataByRowKey(String tableName, String rowKey) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Get get = new Get(rowKey.getBytes());
        StreamingWord streamingWord = new StreamingWord();
        streamingWord.setId(rowKey);
        //先判断是否有此条数据
        if (!get.isCheckExistenceOnly()) {
            Result result = table.get(get);
            for (Cell cell : result.rawCells()) {
                String colName = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
                String value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                if (colName.equals("word")) {
                    streamingWord.setWord(value);
                }
                if (colName.equals("count")) {
                    streamingWord.setCount(Integer.valueOf(value));
                }
                if (colName.equals("topic")) {
                    streamingWord.setTopic(value);
                }
            }
        }
        return streamingWord;
    }

    @Override
    public List getNoDealData(String tableName) {
        List list = new ArrayList();

        try {
            Table table = connection.getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            ResultScanner scanner = table.getScanner(scan);
            for (Result result : scanner) {
                list.add(result);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public String getCellData(String tableName, String rowKey, String family, String col) {
        try {
            Table table = connection.getTable(TableName.valueOf(tableName));
            String result = null;
            Get get = new Get(rowKey.getBytes());
            if (!get.isCheckExistenceOnly()) {
                get.addColumn(Bytes.toBytes(family), Bytes.toBytes(col));
                Result res = table.get(get);
                byte[] resByte = res.getValue(Bytes.toBytes(family), Bytes.toBytes(col));
                return result = Bytes.toString(resByte);
            } else {
                return result = "查询结果不存在";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteByRowKey(String tableName, String rowKey) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        try {
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            //删除指定列
            //delete.addColumns(Bytes.toBytes("contact"), Bytes.toBytes("email"));
            table.delete(delete);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteTable(String tableName) {
        try {
            TableName tablename = TableName.valueOf(tableName);
            admin = connection.getAdmin();
            admin.disableTable(tablename);
            admin.deleteTable(tablename);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
