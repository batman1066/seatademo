CREATE TABLE `stock_tbl`
(
    `id`             int NOT NULL AUTO_INCREMENT,
    `commodity_code` varchar(255) DEFAULT NULL,
    `count`          int          DEFAULT '0',
    PRIMARY KEY (`id`),
    UNIQUE KEY `commodity_code` (`commodity_code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;
CREATE TABLE `storage_change_record`
(
    `id`           int NOT NULL AUTO_INCREMENT,
    `storage_id`   int NOT NULL,
    `business_id`  varchar(100) DEFAULT NULL COMMENT '业务id',
    `src_count`    int NOT NULL COMMENT '原始数据',
    `target_count` int NOT NULL COMMENT '目标数据',
    `seata_type`   int NOT NULL COMMENT '0普通事务 1Tcc 2saga',
    `seata_status` int NOT NULL COMMENT '1准备(TCC) 2提交(012) 3取消(Tcc和saga)',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3 COMMENT='任何变动都记录';
insert into stock_tbl(commodity_code, count)
values ('001', '100');