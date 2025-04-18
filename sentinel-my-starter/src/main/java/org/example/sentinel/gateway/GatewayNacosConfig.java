package org.example.sentinel.gateway;

import com.alibaba.csp.sentinel.adapter.gateway.common.command.UpdateGatewayApiDefinitionGroupCommandHandler;
import com.alibaba.csp.sentinel.adapter.gateway.common.command.UpdateGatewayRuleCommandHandler;
import com.alibaba.nacos.api.config.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.example.sentinel.NacosWriteConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
@ConditionalOnClass(value = {UpdateGatewayApiDefinitionGroupCommandHandler.class, UpdateGatewayRuleCommandHandler.class})
@Slf4j
public class GatewayNacosConfig {
    @Value("${spring.application.name}")
    private String app;
    @Resource
    private NacosWriteConfiguration nacosWriteConfiguration;


    @Bean(name = "gatewayDataSourceInitFunc")
    public GatewayDataSourceInitFunc dataSourceInitFunc(ConfigService configService) throws Exception {
        log.info("sentinel writer开始配置gatewayDataSourceInitFunc和init");
        GatewayDataSourceInitFunc gatewayDataSourceInitFunc = new GatewayDataSourceInitFunc();
        gatewayDataSourceInitFunc.setApp(app);
        gatewayDataSourceInitFunc.setConfigService(configService);
        gatewayDataSourceInitFunc.setNacosWriteConfiguration(nacosWriteConfiguration);
        gatewayDataSourceInitFunc.init();
        return gatewayDataSourceInitFunc;
    }

}