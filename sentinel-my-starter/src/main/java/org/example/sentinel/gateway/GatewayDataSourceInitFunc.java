package org.example.sentinel.gateway;

import com.alibaba.cloud.sentinel.datasource.RuleType;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.command.UpdateGatewayApiDefinitionGroupCommandHandler;
import com.alibaba.csp.sentinel.adapter.gateway.common.command.UpdateGatewayRuleCommandHandler;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.datasource.WritableDataSource;
import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.ConfigService;
import lombok.Data;
import org.example.sentinel.NacosWritableDataSource;
import org.example.sentinel.NacosWriteConfiguration;

import java.util.Set;

@Data
public class GatewayDataSourceInitFunc implements InitFunc {
    private String app;
    private ConfigService configService;
    private NacosWriteConfiguration nacosWriteConfiguration;

    public void init() throws Exception {
        //网关api分组规则
        WritableDataSource<Set<ApiDefinition>> gatewayApiDefinitionGroupRuleWDS = new NacosWritableDataSource<>(
                app,
                nacosWriteConfiguration.getGroupId(),
                "-" + RuleType.GW_API_GROUP.getName() + "-rules",
                configService,
                this::encodeJson
        );
        UpdateGatewayApiDefinitionGroupCommandHandler.setWritableDataSource(gatewayApiDefinitionGroupRuleWDS);
        //网关流控规则
        WritableDataSource<Set<GatewayFlowRule>> gatewayFlowRuleWDS = new NacosWritableDataSource<>(
                app,
                nacosWriteConfiguration.getGroupId(),
                "-" + RuleType.GW_FLOW.getName() + "-rules",
                configService,
                this::encodeJson
        );
        UpdateGatewayRuleCommandHandler.setWritableDataSource(gatewayFlowRuleWDS);
    }

    private <T> String encodeJson(T t) {
        return JSON.toJSONString(t);
    }
}
