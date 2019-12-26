package io.byr.streaming.spart_kafka.config;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

/**
 * @ClassName: HBaseConfig
 * @Description: TODO
 * @Author: yanrong
 * @Date: 2019/11/28 13:38
 */

@org.springframework.context.annotation.Configuration
public class HBaseConfig {
    @Autowired
    private HBaseConfigBean hBaseConfigBean;

    @Bean
    public Connection connection() throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = null;
        // HBaseIP地址
        try {
            configuration.set("hbase.zookeeper.quorum", hBaseConfigBean.getHbaseIp());
            //HBase端口号
            configuration.set("hbase.zookeeper.property.clientPort", hBaseConfigBean.getHbasePort());
            configuration.set("zookeeper.znode.parent", "/hbase");
            connection = ConnectionFactory.createConnection(configuration);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
    @Bean
    public Admin getAdmin() throws IOException {
        Admin admin=this.connection().getAdmin();
        return admin;
    }
}
