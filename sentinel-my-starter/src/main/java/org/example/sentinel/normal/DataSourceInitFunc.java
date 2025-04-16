package org.example.sentinel.normal;

import com.alibaba.cloud.sentinel.datasource.RuleType;
import com.alibaba.csp.sentinel.command.handler.ModifyParamFlowRulesCommandHandler;
import com.alibaba.csp.sentinel.datasource.WritableDataSource;
import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.transport.util.WritableDataSourceRegistry;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.ConfigService;
import lombok.Data;
import org.example.sentinel.NacosWritableDataSource;
import org.example.sentinel.NacosWriteConfiguration;

import java.util.List;

@Data
public class DataSourceInitFunc implements InitFunc {
    private String app;
    private ConfigService configService;
    private NacosWriteConfiguration nacosWriteConfiguration;

    public void init() throws Exception {
        // 流控规则
        WritableDataSource<List<FlowRule>> flowRuleWDS = new NacosWritableDataSource<>(
                app,
                nacosWriteConfiguration.getGroupId(),
                "-" + RuleType.FLOW.getName() + "-rules",
                configService,
                this::encodeJson
        );
        // 将可写数据源注册至transport模块的WritableDataSourceRegistry中
        // 这样收到控制台推送的规则时，Sentinel会先更新到内存，然后将规则写入到文件中
        WritableDataSourceRegistry.registerFlowDataSource(flowRuleWDS);

        // 降级规则
        WritableDataSource<List<DegradeRule>> degradeRuleWDS = new NacosWritableDataSource<>(
                app,
                nacosWriteConfiguration.getGroupId(),
                "-" + RuleType.DEGRADE.getName() + "-rules",
                configService,
                this::encodeJson
        );
        WritableDataSourceRegistry.registerDegradeDataSource(degradeRuleWDS);

        // 系统规则
        WritableDataSource<List<SystemRule>> systemRuleWDS = new NacosWritableDataSource<>(
                app,
                nacosWriteConfiguration.getGroupId(),
                "-" + RuleType.SYSTEM.getName() + "-rules",
                configService,
                this::encodeJson
        );
        WritableDataSourceRegistry.registerSystemDataSource(systemRuleWDS);

        // 授权规则
        WritableDataSource<List<AuthorityRule>> authorityRuleWDS = new NacosWritableDataSource<>(
                app,
                nacosWriteConfiguration.getGroupId(),
                "-" + RuleType.AUTHORITY.getName() + "-rules",
                configService,
                this::encodeJson
        );
        WritableDataSourceRegistry.registerAuthorityDataSource(authorityRuleWDS);
        // 热点参数规则
        WritableDataSource<List<ParamFlowRule>> paramFlowRuleWDS = new NacosWritableDataSource<>(
                app,
                nacosWriteConfiguration.getGroupId(),
                "-" + RuleType.PARAM_FLOW.getName() + "-rules",
                configService,
                this::encodeJson
        );
        ModifyParamFlowRulesCommandHandler.setWritableDataSource(paramFlowRuleWDS);
       
    }

    private <T> String encodeJson(T t) {
        return JSON.toJSONString(t);
    }
}
