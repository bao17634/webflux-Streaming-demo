package io.byr.streaming.spart_kafka.config;

import org.apache.kudu.client.KuduClient;
import org.apache.kudu.client.KuduSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: KuduConfig
 * @Description: TODO
 * @Author: yanrong
 * @Date: 2019/12/11 11:49
 */
@Configuration
public class KuduConfig {
    @Autowired
    private KuduConfigBean kuduConfigBean;
    @Bean
    public KuduClient KuduClientInit(){
        KuduClient client = new KuduClient.KuduClientBuilder(kuduConfigBean.getKuduIp())
                .defaultAdminOperationTimeoutMs(60000).build();
        return client;
    }
    @Bean
    public KuduSession kuduSession(){
        KuduClient  client=this.KuduClientInit();
        KuduSession session=client.newSession();
        session.setTimeoutMillis(60000);
        return session;
    }
}
