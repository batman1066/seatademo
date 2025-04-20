package org.example.order.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 任何变动都记录
 *
 * @TableName order_change_record
 */
@TableName(value = "order_change_record")
@Data
public class OrderChangeRecordDO implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *
     */
    private Long orderId;

    /**
     * 业务id
     */
    private String businessId;

    /**
     * 原始数据
     */
    private Integer srcStatus;

    /**
     * 目标数据
     */
    private Integer targetStatus;

    /**
     * 0普通事务 1Tcc 2saga
     */
    private Integer seataType;

    /**
     * 1准备(TCC) 2提交(012) 3取消(Tcc和saga)
     */
    private Integer seataStatus;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}