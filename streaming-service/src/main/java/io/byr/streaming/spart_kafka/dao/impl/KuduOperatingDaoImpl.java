package io.byr.streaming.spart_kafka.dao.impl;

import io.byr.streaming.spart_kafka.dao.KuduOperatingDao;
import io.byr.streaming.spart_kafka.entity.StreamingWord;
import lombok.extern.slf4j.Slf4j;
import org.apache.kudu.ColumnSchema;
import org.apache.kudu.Schema;
import org.apache.kudu.Type;
import org.apache.kudu.client.*;
import org.apache.kudu.shaded.org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName: KuduOperatingDaoImpl
 * @Description: TODO
 * @Author: yanrong
 * @Date: 2019/12/9 12:23
 */
@Slf4j
@Repository("Kudu")
public class KuduOperatingDaoImpl implements KuduOperatingDao {
    @Autowired
    private KuduClient client;
    @Autowired
    private KuduSession session;

    @Override
    public KuduTable createTable(String tableName) throws KuduException {
        KuduTable kuduTable = null;
        List<ColumnSchema> columns = new ArrayList();
        columns.add(new ColumnSchema.ColumnSchemaBuilder("key", Type.STRING)
                .key(true)
                .build());
        columns.add(new ColumnSchema.ColumnSchemaBuilder("word", Type.STRING)
                .key(true)
                .build());
        columns.add(new ColumnSchema.ColumnSchemaBuilder("count", Type.STRING)
                .build());
        columns.add(new ColumnSchema.ColumnSchemaBuilder("topic", Type.STRING)
                .build());
        List<String> rangeKeys = new ArrayList<>();
        rangeKeys.add("key");
        Schema schema = new Schema(columns);
        try {
            if (!client.tableExists(tableName)) {
                kuduTable = client.createTable(tableName, schema, new CreateTableOptions().setRangePartitionColumns(rangeKeys));
            }
            log.info("------------insert start--------------");
            //获得表的列信息;
            return kuduTable;
        } catch (KuduException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public OperationResponse insertData(String tableName, StreamingWord streamingWord) throws KuduException {
        this.createTable(tableName);
        try {
            KuduTable table = client.openTable(tableName);
            Insert insert = table.newInsert();
            PartialRow row = insert.getRow();
            row.addString(0, UUID.randomUUID().toString().substring(1, 4));
            row.addString(1, streamingWord.getWord());
            row.addString(2, String.valueOf(streamingWord.getCount()));
            row.addString(3, streamingWord.getTopic());
            OperationResponse operationResponse = session.apply(insert);
            return operationResponse;
        } catch (KuduException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public OperationResponse deleteDataByKey(String tableName, String key) throws KuduException {
        try {
            KuduTable table = client.openTable(tableName);
            Delete delete = table.newDelete();
            PartialRow row = delete.getRow();
            row.addInt("key", 0);
            OperationResponse apply = session.apply(delete);
            return apply;
        } catch (KuduException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public OperationResponse updateDataByKey(String tableName, String key) throws KuduException {
        try {
            KuduTable table = client.openTable(tableName);
            Update update = table.newUpdate();
            PartialRow row1 = update.getRow();
            row1.addInt("key", 6);
            row1.addString("value", "kexin");
            OperationResponse operationResponse = session.apply(update);
            return operationResponse;
        } catch (KuduException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> listData(String tableName,List<String> listColumns) throws KuduException {
        List<String> list = new ArrayList<>();
        try {
            KuduTable table = client.openTable(tableName);
            KuduScanner scanner = client.newScannerBuilder(table)
                    //设置扫描的哪些列
                    .setProjectedColumnNames(listColumns)
                    .build();
            while (scanner.hasMoreRows()) {
                RowResultIterator results = scanner.nextRows();
                while (results.hasNext()) {
                    RowResult result = results.next();
                    list.add(result.getString(0));
                    list.add(result.getString(1));
                    list.add(result.getString(2));
                    list.add(result.getString(3));
                }
            }
            return list;
        } catch (KuduException e) {
            throw new RuntimeException(e);
        }
    }
}
