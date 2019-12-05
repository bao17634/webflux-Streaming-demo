package io.byr.streaming.spart_kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName: HBaseBean
 * @Description: TODO
 * @Author: yanrong
 * @Date: 2019/11/27 19:30
 */
@Data
@Component
@ConfigurationProperties(prefix = "hbase")
public class HBaseConfigBean {
    /**
     * hbase ip地址
     */
    private String hbaseIp;
    /**
     * hbase 端口号
     */
    private String hbasePort;
}
