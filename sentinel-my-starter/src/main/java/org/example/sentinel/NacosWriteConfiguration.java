package org.example.sentinel;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sentinel.write.nacos")
@Data
public class NacosWriteConfiguration {
    private String serverAddr;
    private String dataId;
    private String groupId;
    private String namespace;
    private String username;
    private String password;
    // 自行补充get与set方法
}
