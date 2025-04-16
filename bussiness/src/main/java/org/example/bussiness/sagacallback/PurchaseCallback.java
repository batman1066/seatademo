package org.example.bussiness.sagacallback;

import lombok.extern.slf4j.Slf4j;
import org.apache.seata.saga.engine.AsyncCallback;
import org.apache.seata.saga.proctrl.ProcessContext;
import org.apache.seata.saga.statelang.domain.StateMachineInstance;

/**
 * PurchaseCallback
 *
 * @author yangming
 * @date 2025/4/9 11:53
 **/
@Slf4j
public class PurchaseCallback implements AsyncCallback {
    /**
     * on finished
     *
     * @param context
     * @param stateMachineInstance
     */
    @Override
    public void onFinished(ProcessContext context, StateMachineInstance stateMachineInstance) {
        log.info("saga transaction onFinished XID:{},状态机状态：{},补偿状态:{}",
                stateMachineInstance.getId(), stateMachineInstance.getStatus().getStatusString(),
                stateMachineInstance.getCompensationStatus().getStatusString());
    }

    /**
     * on error
     *
     * @param context
     * @param stateMachineInstance
     * @param exp
     */
    @Override
    public void onError(ProcessContext context, StateMachineInstance stateMachineInstance, Exception exp) {
        log.info("saga transaction onError XID:{},状态机状态：{},补偿状态:{}",
                stateMachineInstance.getId(), stateMachineInstance.getStatus().getStatusString(),
                stateMachineInstance.getCompensationStatus().getStatusString());
    }
}
