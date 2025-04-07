CREATE TABLE `account_tbl`
(
    `id`      int NOT NULL AUTO_INCREMENT,
    `user_id` varchar(255) DEFAULT NULL,
    `money`   int          DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;
CREATE TABLE `account_change_record`
(
    `id`           int NOT NULL AUTO_INCREMENT,
    `account_id`   int NOT NULL,
    `business_id`  varchar(100) DEFAULT NULL COMMENT '业务id',
    `src_money`    int NOT NULL COMMENT '原始数据',
    `target_money` int NOT NULL COMMENT '目标数据',
    `seata_type`   int NOT NULL COMMENT '0普通事务 1Tcc 2saga',
    `seata_status` int NOT NULL COMMENT '1准备(TCC) 2提交(012) 3取消(Tcc和saga)',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb3 COMMENT='任何变动都记录';

insert into account_tbl(user_id, money)
values (1, '10000');