package org.example.sentinel.normal;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigFactory;
import com.alibaba.nacos.api.config.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.example.sentinel.NacosWriteConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Properties;

@Configuration
@Slf4j
public class NacosConfig {
    @Value("${spring.application.name}")
    private String app;
    @Resource
    private NacosWriteConfiguration nacosWriteConfiguration;

    @Bean(name = "nacosConfigServiceForWrite")
    public ConfigService nacosConfigService(NacosWriteConfiguration nacosWriteConfiguration) throws Exception {
        log.info("sentinel writer开始配置nacosConfigService,nacosWriteConfiguration:{}", JSON.toJSONString(nacosWriteConfiguration));
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, nacosWriteConfiguration.getServerAddr());
        properties.put(PropertyKeyConst.NAMESPACE, nacosWriteConfiguration.getNamespace());
        properties.put(PropertyKeyConst.USERNAME, nacosWriteConfiguration.getUsername());
        properties.put(PropertyKeyConst.PASSWORD, nacosWriteConfiguration.getPassword());
        return ConfigFactory.createConfigService(properties);
    }

    @Bean(name = "dataSourceInitFunc")
    public DataSourceInitFunc dataSourceInitFunc(ConfigService configService) throws Exception {
        log.info("sentinel writer开始配置dataSourceInitFunc和init");
        DataSourceInitFunc dataSourceInitFunc = new DataSourceInitFunc();
        dataSourceInitFunc.setApp(app);
        dataSourceInitFunc.setConfigService(configService);
        dataSourceInitFunc.setNacosWriteConfiguration(nacosWriteConfiguration);
        dataSourceInitFunc.init();
        return dataSourceInitFunc;
    }

}