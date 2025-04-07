package org.example.storage.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 任何变动都记录
 * @TableName storage_change_record
 */
@TableName(value ="storage_change_record")
@Data
public class StorageChangeRecordDO implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private Integer storageId;

    /**
     * 业务id
     */
    private String businessId;

    /**
     * 原始数据
     */
    private Integer srcCount;

    /**
     * 目标数据
     */
    private Integer targetCount;

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