package io.byr.streaming.spart_kafka.controller;

import org.apache.kudu.ColumnSchema;
import org.apache.kudu.Schema;
import org.apache.kudu.Type;
import org.apache.kudu.client.*;

import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: KuduController
 * @Description: TODO
 * @Author: yanrong
 * @Date: 2019/12/9 11:54
 */


public class KuduController {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(KuduController.class);
    private static final String KUDU_MASTER = "192.168.61.129";

    public static void main(String[] args) {

        System.out.println("-----------------------------------------------");
        System.out.println("Will try to connect to Kudu master at " + KUDU_MASTER);
        System.out.println("-----------------------------------------------");
        String tableName = "kuduTest";
        KuduClient client = new KuduClient.KuduClientBuilder(KUDU_MASTER)
                .defaultAdminOperationTimeoutMs(60000).build();

        try {
            logger.info("------------create start--------------");
            /**
             * 创建表
             */
            List<ColumnSchema> columns = new ArrayList(2);
            columns.add(new ColumnSchema.ColumnSchemaBuilder("key", Type.INT32)
                    .key(true)
                    .build());
            columns.add(new ColumnSchema.ColumnSchemaBuilder("value", Type.STRING)
                    .build());
            List<String> rangeKeys = new ArrayList<>();
            rangeKeys.add("key");
            Schema schema = new Schema(columns);
            if(!client.tableExists(tableName)){
                client.createTable(tableName, schema, new CreateTableOptions().setRangePartitionColumns(rangeKeys));
                logger.info("------------insert start--------------");
                client.getTablesList().getTablesList().forEach(str -> System.out.println(str));
            }
            /**
             * 表中插入数据
             */
            KuduTable table = client.openTable(tableName);
            //创建一个与集群交互的新会话。用户负责销毁会话对象。这是一个完全的本地操作(没有rpc或阻塞)。
            KuduSession session = client.newSession();
            session.setTimeoutMillis(60000);
            for (int i = 0; i < 3; i++) {
                logger.info("----------------insert  " + i + "---------------");
                //使用此表的模式配置一个新的插入。
                //返回的对象不应该被重用。
                Insert insert = table.newInsert();
                PartialRow row = insert.getRow();
                row.addInt(0, i);
                row.addString(1, "value " + i);
                session.apply(insert);
            }

            logger.info("------------delete data start--------------");
            /**
             * 根据主键删除数据
             */
            Delete delete = table.newDelete();
            PartialRow row = delete.getRow();
            row.addInt("key", 0);
            OperationResponse apply = session.apply(delete);
            if (apply.hasRowError()) {
                logger.info("------------delete fail--------------");
            } else {
                logger.info("------------delete success--------------");
            }

            logger.info("------------update start--------------");
            /**
             * 更新数据
             */
            Update update = table.newUpdate();
            PartialRow row1 = update.getRow();
            row1.addInt("key", 6);
            row1.addString("value", "kexin");
            session.apply(update);

            logger.info("------------scan start--------------");
            /**
             * 扫描查找数据
             */
            List<String> projectColumns = new ArrayList<>(1);
            projectColumns.add("value");
            KuduScanner scanner = client.newScannerBuilder(table)
                    .setProjectedColumnNames(projectColumns)
                    .build();
            while (scanner.hasMoreRows()) {
                RowResultIterator results = scanner.nextRows();
                while (results.hasNext()) {
                    RowResult result = results.next();
                    System.out.println(result.getString(0));
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                logger.info("------------delete table start--------------");
                //删除表
//                client.deleteTable(tableName);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    client.shutdown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


