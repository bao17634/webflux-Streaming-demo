package io.byr.streaming.spart_kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName: KuduConfigBean
 * @Description: TODO
 * @Author: yanrong
 * @Date: 2019/12/11 11:46
 */
@Data
@Component
@ConfigurationProperties(prefix = "kudu")
public class KuduConfigBean {
    /**
     * kudu 服务器地址
     */
    private String kuduIp;
    /**
     * kudu 端口号
     */
    private String kuduPort;

}
