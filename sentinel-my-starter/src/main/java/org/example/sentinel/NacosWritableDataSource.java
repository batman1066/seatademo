package org.example.sentinel;

import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.datasource.WritableDataSource;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.ConfigType;
import lombok.Data;

@Data
public class NacosWritableDataSource<T> implements WritableDataSource<T> {
    private String app;
    private String dataIdPostfix;
    private String groupId;
    private ConfigService configService;
    private Converter<T, String> configEncoder;

    public NacosWritableDataSource(String app, String groupId, String dataIdPostfix, ConfigService configService, Converter<T, String> configEncoder) {
        this.app = app;
        this.dataIdPostfix = dataIdPostfix;
        this.configService = configService;
        this.configEncoder = configEncoder;
    }

    @Override
    public void write(T value) throws Exception {
        String convertResult = configEncoder.convert(value);
        configService.publishConfig(app + dataIdPostfix,
                groupId, convertResult, ConfigType.JSON.getType());
    }

    @Override
    public void close() throws Exception {

    }
}
