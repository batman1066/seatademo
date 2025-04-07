package org.example.storage.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName stock_tbl
 */
@TableName(value ="stock_tbl")
@Data
public class StockTblDO implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String commodityCode;

    /**
     * 
     */
    private Integer count;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}