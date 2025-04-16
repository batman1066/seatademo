package org.example.dubbo;

import com.alibaba.csp.sentinel.adapter.dubbo3.config.DubboAdapterGlobalConfig;
import org.apache.dubbo.rpc.AsyncRpcResult;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class GlobalDubboInit {
    @PostConstruct
    public void init() {
        DubboAdapterGlobalConfig.setProviderFallback((invoker, invocation, ex) -> {
            System.out.println("Blocked by Sentinel: " + ex.getClass().getSimpleName() + ", " + invocation);
            return AsyncRpcResult.newDefaultAsyncResult(ex.toRuntimeException(), invocation);
        });
        DubboAdapterGlobalConfig.setConsumerFallback((invoker, invocation, ex) -> {
            System.out.println("Blocked by Sentinel: " + ex.getClass().getSimpleName() + ", " + invocation);

            return AsyncRpcResult.newDefaultAsyncResult(ex.toRuntimeException(), invocation);
        });
    }
}
