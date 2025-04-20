package org.example.order.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName order_tbl
 */
@TableName(value = "order_tbl")
@Data
public class OrderTblDO implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *
     */
    private String userId;

    /**
     *
     */
    private String commodityCode;

    /**
     *
     */
    private Integer count;

    /**
     *
     */
    private Integer money;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}