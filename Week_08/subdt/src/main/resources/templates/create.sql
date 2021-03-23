CREATE TABLE `dt_order0`  (
  `order_id` bigint(20) UNSIGNED NOT NULL COMMENT '订单id',
  `order_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '订单号',
  `buyer_id` int(11) NOT NULL COMMENT '买家id',
  `order_status` int(11) NOT NULL COMMENT '订单状态',
  `order_price_i` int(11) NOT NULL COMMENT '订单金额整数位',
  `order_price_d` int(11) NOT NULL COMMENT '订单金额小数位',
  `pay_price_i` int(11) NULL DEFAULT NULL COMMENT '实付金额整数位',
  `pay_price_d` int(11) NULL DEFAULT NULL COMMENT '实付金额小数位',
  `pay_complete_time` timestamp(0) NULL DEFAULT NULL COMMENT '支付完成时间',
  `order_complete_time` timestamp(0) NULL DEFAULT NULL COMMENT '订单完成时间',
  `create_time` timestamp(0) NOT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NOT NULL COMMENT '最后更新时间',
  `delete_tag` bit(1) NULL DEFAULT b'0' COMMENT '删除标记',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

CREATE TABLE `dt_order1`  (
  `order_id` bigint(20) UNSIGNED NOT NULL COMMENT '订单id',
  `order_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '订单号',
  `buyer_id` int(11) NOT NULL COMMENT '买家id',
  `order_status` int(11) NOT NULL COMMENT '订单状态',
  `order_price_i` int(11) NOT NULL COMMENT '订单金额整数位',
  `order_price_d` int(11) NOT NULL COMMENT '订单金额小数位',
  `pay_price_i` int(11) NULL DEFAULT NULL COMMENT '实付金额整数位',
  `pay_price_d` int(11) NULL DEFAULT NULL COMMENT '实付金额小数位',
  `pay_complete_time` timestamp(0) NULL DEFAULT NULL COMMENT '支付完成时间',
  `order_complete_time` timestamp(0) NULL DEFAULT NULL COMMENT '订单完成时间',
  `create_time` timestamp(0) NOT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NOT NULL COMMENT '最后更新时间',
  `delete_tag` bit(1) NULL DEFAULT b'0' COMMENT '删除标记',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

CREATE TABLE `dt_order2`  (
  `order_id` bigint(20) UNSIGNED NOT NULL COMMENT '订单id',
  `order_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '订单号',
  `buyer_id` int(11) NOT NULL COMMENT '买家id',
  `order_status` int(11) NOT NULL COMMENT '订单状态',
  `order_price_i` int(11) NOT NULL COMMENT '订单金额整数位',
  `order_price_d` int(11) NOT NULL COMMENT '订单金额小数位',
  `pay_price_i` int(11) NULL DEFAULT NULL COMMENT '实付金额整数位',
  `pay_price_d` int(11) NULL DEFAULT NULL COMMENT '实付金额小数位',
  `pay_complete_time` timestamp(0) NULL DEFAULT NULL COMMENT '支付完成时间',
  `order_complete_time` timestamp(0) NULL DEFAULT NULL COMMENT '订单完成时间',
  `create_time` timestamp(0) NOT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NOT NULL COMMENT '最后更新时间',
  `delete_tag` bit(1) NULL DEFAULT b'0' COMMENT '删除标记',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

CREATE TABLE `dt_order3`  (
  `order_id` bigint(20) UNSIGNED NOT NULL COMMENT '订单id',
  `order_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '订单号',
  `buyer_id` int(11) NOT NULL COMMENT '买家id',
  `order_status` int(11) NOT NULL COMMENT '订单状态',
  `order_price_i` int(11) NOT NULL COMMENT '订单金额整数位',
  `order_price_d` int(11) NOT NULL COMMENT '订单金额小数位',
  `pay_price_i` int(11) NULL DEFAULT NULL COMMENT '实付金额整数位',
  `pay_price_d` int(11) NULL DEFAULT NULL COMMENT '实付金额小数位',
  `pay_complete_time` timestamp(0) NULL DEFAULT NULL COMMENT '支付完成时间',
  `order_complete_time` timestamp(0) NULL DEFAULT NULL COMMENT '订单完成时间',
  `create_time` timestamp(0) NOT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NOT NULL COMMENT '最后更新时间',
  `delete_tag` bit(1) NULL DEFAULT b'0' COMMENT '删除标记',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

CREATE TABLE `dt_order4`  (
  `order_id` bigint(20) UNSIGNED NOT NULL COMMENT '订单id',
  `order_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '订单号',
  `buyer_id` int(11) NOT NULL COMMENT '买家id',
  `order_status` int(11) NOT NULL COMMENT '订单状态',
  `order_price_i` int(11) NOT NULL COMMENT '订单金额整数位',
  `order_price_d` int(11) NOT NULL COMMENT '订单金额小数位',
  `pay_price_i` int(11) NULL DEFAULT NULL COMMENT '实付金额整数位',
  `pay_price_d` int(11) NULL DEFAULT NULL COMMENT '实付金额小数位',
  `pay_complete_time` timestamp(0) NULL DEFAULT NULL COMMENT '支付完成时间',
  `order_complete_time` timestamp(0) NULL DEFAULT NULL COMMENT '订单完成时间',
  `create_time` timestamp(0) NOT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NOT NULL COMMENT '最后更新时间',
  `delete_tag` bit(1) NULL DEFAULT b'0' COMMENT '删除标记',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

CREATE TABLE `dt_order5`  (
  `order_id` bigint(20) UNSIGNED NOT NULL COMMENT '订单id',
  `order_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '订单号',
  `buyer_id` int(11) NOT NULL COMMENT '买家id',
  `order_status` int(11) NOT NULL COMMENT '订单状态',
  `order_price_i` int(11) NOT NULL COMMENT '订单金额整数位',
  `order_price_d` int(11) NOT NULL COMMENT '订单金额小数位',
  `pay_price_i` int(11) NULL DEFAULT NULL COMMENT '实付金额整数位',
  `pay_price_d` int(11) NULL DEFAULT NULL COMMENT '实付金额小数位',
  `pay_complete_time` timestamp(0) NULL DEFAULT NULL COMMENT '支付完成时间',
  `order_complete_time` timestamp(0) NULL DEFAULT NULL COMMENT '订单完成时间',
  `create_time` timestamp(0) NOT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NOT NULL COMMENT '最后更新时间',
  `delete_tag` bit(1) NULL DEFAULT b'0' COMMENT '删除标记',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

CREATE TABLE `dt_order6`  (
  `order_id` bigint(20) UNSIGNED NOT NULL COMMENT '订单id',
  `order_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '订单号',
  `buyer_id` int(11) NOT NULL COMMENT '买家id',
  `order_status` int(11) NOT NULL COMMENT '订单状态',
  `order_price_i` int(11) NOT NULL COMMENT '订单金额整数位',
  `order_price_d` int(11) NOT NULL COMMENT '订单金额小数位',
  `pay_price_i` int(11) NULL DEFAULT NULL COMMENT '实付金额整数位',
  `pay_price_d` int(11) NULL DEFAULT NULL COMMENT '实付金额小数位',
  `pay_complete_time` timestamp(0) NULL DEFAULT NULL COMMENT '支付完成时间',
  `order_complete_time` timestamp(0) NULL DEFAULT NULL COMMENT '订单完成时间',
  `create_time` timestamp(0) NOT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NOT NULL COMMENT '最后更新时间',
  `delete_tag` bit(1) NULL DEFAULT b'0' COMMENT '删除标记',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

CREATE TABLE `dt_order7`  (
  `order_id` bigint(20) UNSIGNED NOT NULL COMMENT '订单id',
  `order_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '订单号',
  `buyer_id` int(11) NOT NULL COMMENT '买家id',
  `order_status` int(11) NOT NULL COMMENT '订单状态',
  `order_price_i` int(11) NOT NULL COMMENT '订单金额整数位',
  `order_price_d` int(11) NOT NULL COMMENT '订单金额小数位',
  `pay_price_i` int(11) NULL DEFAULT NULL COMMENT '实付金额整数位',
  `pay_price_d` int(11) NULL DEFAULT NULL COMMENT '实付金额小数位',
  `pay_complete_time` timestamp(0) NULL DEFAULT NULL COMMENT '支付完成时间',
  `order_complete_time` timestamp(0) NULL DEFAULT NULL COMMENT '订单完成时间',
  `create_time` timestamp(0) NOT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NOT NULL COMMENT '最后更新时间',
  `delete_tag` bit(1) NULL DEFAULT b'0' COMMENT '删除标记',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

CREATE TABLE `dt_order8`  (
  `order_id` bigint(20) UNSIGNED NOT NULL COMMENT '订单id',
  `order_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '订单号',
  `buyer_id` int(11) NOT NULL COMMENT '买家id',
  `order_status` int(11) NOT NULL COMMENT '订单状态',
  `order_price_i` int(11) NOT NULL COMMENT '订单金额整数位',
  `order_price_d` int(11) NOT NULL COMMENT '订单金额小数位',
  `pay_price_i` int(11) NULL DEFAULT NULL COMMENT '实付金额整数位',
  `pay_price_d` int(11) NULL DEFAULT NULL COMMENT '实付金额小数位',
  `pay_complete_time` timestamp(0) NULL DEFAULT NULL COMMENT '支付完成时间',
  `order_complete_time` timestamp(0) NULL DEFAULT NULL COMMENT '订单完成时间',
  `create_time` timestamp(0) NOT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NOT NULL COMMENT '最后更新时间',
  `delete_tag` bit(1) NULL DEFAULT b'0' COMMENT '删除标记',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

CREATE TABLE `dt_order9`  (
  `order_id` bigint(20) UNSIGNED NOT NULL COMMENT '订单id',
  `order_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '订单号',
  `buyer_id` int(11) NOT NULL COMMENT '买家id',
  `order_status` int(11) NOT NULL COMMENT '订单状态',
  `order_price_i` int(11) NOT NULL COMMENT '订单金额整数位',
  `order_price_d` int(11) NOT NULL COMMENT '订单金额小数位',
  `pay_price_i` int(11) NULL DEFAULT NULL COMMENT '实付金额整数位',
  `pay_price_d` int(11) NULL DEFAULT NULL COMMENT '实付金额小数位',
  `pay_complete_time` timestamp(0) NULL DEFAULT NULL COMMENT '支付完成时间',
  `order_complete_time` timestamp(0) NULL DEFAULT NULL COMMENT '订单完成时间',
  `create_time` timestamp(0) NOT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NOT NULL COMMENT '最后更新时间',
  `delete_tag` bit(1) NULL DEFAULT b'0' COMMENT '删除标记',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

CREATE TABLE `dt_order10`  (
  `order_id` bigint(20) UNSIGNED NOT NULL COMMENT '订单id',
  `order_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '订单号',
  `buyer_id` int(11) NOT NULL COMMENT '买家id',
  `order_status` int(11) NOT NULL COMMENT '订单状态',
  `order_price_i` int(11) NOT NULL COMMENT '订单金额整数位',
  `order_price_d` int(11) NOT NULL COMMENT '订单金额小数位',
  `pay_price_i` int(11) NULL DEFAULT NULL COMMENT '实付金额整数位',
  `pay_price_d` int(11) NULL DEFAULT NULL COMMENT '实付金额小数位',
  `pay_complete_time` timestamp(0) NULL DEFAULT NULL COMMENT '支付完成时间',
  `order_complete_time` timestamp(0) NULL DEFAULT NULL COMMENT '订单完成时间',
  `create_time` timestamp(0) NOT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NOT NULL COMMENT '最后更新时间',
  `delete_tag` bit(1) NULL DEFAULT b'0' COMMENT '删除标记',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

CREATE TABLE `dt_order11`  (
  `order_id` bigint(20) UNSIGNED NOT NULL COMMENT '订单id',
  `order_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '订单号',
  `buyer_id` int(11) NOT NULL COMMENT '买家id',
  `order_status` int(11) NOT NULL COMMENT '订单状态',
  `order_price_i` int(11) NOT NULL COMMENT '订单金额整数位',
  `order_price_d` int(11) NOT NULL COMMENT '订单金额小数位',
  `pay_price_i` int(11) NULL DEFAULT NULL COMMENT '实付金额整数位',
  `pay_price_d` int(11) NULL DEFAULT NULL COMMENT '实付金额小数位',
  `pay_complete_time` timestamp(0) NULL DEFAULT NULL COMMENT '支付完成时间',
  `order_complete_time` timestamp(0) NULL DEFAULT NULL COMMENT '订单完成时间',
  `create_time` timestamp(0) NOT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NOT NULL COMMENT '最后更新时间',
  `delete_tag` bit(1) NULL DEFAULT b'0' COMMENT '删除标记',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

CREATE TABLE `dt_order12`  (
  `order_id` bigint(20) UNSIGNED NOT NULL COMMENT '订单id',
  `order_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '订单号',
  `buyer_id` int(11) NOT NULL COMMENT '买家id',
  `order_status` int(11) NOT NULL COMMENT '订单状态',
  `order_price_i` int(11) NOT NULL COMMENT '订单金额整数位',
  `order_price_d` int(11) NOT NULL COMMENT '订单金额小数位',
  `pay_price_i` int(11) NULL DEFAULT NULL COMMENT '实付金额整数位',
  `pay_price_d` int(11) NULL DEFAULT NULL COMMENT '实付金额小数位',
  `pay_complete_time` timestamp(0) NULL DEFAULT NULL COMMENT '支付完成时间',
  `order_complete_time` timestamp(0) NULL DEFAULT NULL COMMENT '订单完成时间',
  `create_time` timestamp(0) NOT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NOT NULL COMMENT '最后更新时间',
  `delete_tag` bit(1) NULL DEFAULT b'0' COMMENT '删除标记',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

CREATE TABLE `dt_order13`  (
  `order_id` bigint(20) UNSIGNED NOT NULL COMMENT '订单id',
  `order_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '订单号',
  `buyer_id` int(11) NOT NULL COMMENT '买家id',
  `order_status` int(11) NOT NULL COMMENT '订单状态',
  `order_price_i` int(11) NOT NULL COMMENT '订单金额整数位',
  `order_price_d` int(11) NOT NULL COMMENT '订单金额小数位',
  `pay_price_i` int(11) NULL DEFAULT NULL COMMENT '实付金额整数位',
  `pay_price_d` int(11) NULL DEFAULT NULL COMMENT '实付金额小数位',
  `pay_complete_time` timestamp(0) NULL DEFAULT NULL COMMENT '支付完成时间',
  `order_complete_time` timestamp(0) NULL DEFAULT NULL COMMENT '订单完成时间',
  `create_time` timestamp(0) NOT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NOT NULL COMMENT '最后更新时间',
  `delete_tag` bit(1) NULL DEFAULT b'0' COMMENT '删除标记',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

CREATE TABLE `dt_order14`  (
  `order_id` bigint(20) UNSIGNED NOT NULL COMMENT '订单id',
  `order_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '订单号',
  `buyer_id` int(11) NOT NULL COMMENT '买家id',
  `order_status` int(11) NOT NULL COMMENT '订单状态',
  `order_price_i` int(11) NOT NULL COMMENT '订单金额整数位',
  `order_price_d` int(11) NOT NULL COMMENT '订单金额小数位',
  `pay_price_i` int(11) NULL DEFAULT NULL COMMENT '实付金额整数位',
  `pay_price_d` int(11) NULL DEFAULT NULL COMMENT '实付金额小数位',
  `pay_complete_time` timestamp(0) NULL DEFAULT NULL COMMENT '支付完成时间',
  `order_complete_time` timestamp(0) NULL DEFAULT NULL COMMENT '订单完成时间',
  `create_time` timestamp(0) NOT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NOT NULL COMMENT '最后更新时间',
  `delete_tag` bit(1) NULL DEFAULT b'0' COMMENT '删除标记',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

CREATE TABLE `dt_order15`  (
  `order_id` bigint(20) UNSIGNED NOT NULL COMMENT '订单id',
  `order_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '订单号',
  `buyer_id` int(11) NOT NULL COMMENT '买家id',
  `order_status` int(11) NOT NULL COMMENT '订单状态',
  `order_price_i` int(11) NOT NULL COMMENT '订单金额整数位',
  `order_price_d` int(11) NOT NULL COMMENT '订单金额小数位',
  `pay_price_i` int(11) NULL DEFAULT NULL COMMENT '实付金额整数位',
  `pay_price_d` int(11) NULL DEFAULT NULL COMMENT '实付金额小数位',
  `pay_complete_time` timestamp(0) NULL DEFAULT NULL COMMENT '支付完成时间',
  `order_complete_time` timestamp(0) NULL DEFAULT NULL COMMENT '订单完成时间',
  `create_time` timestamp(0) NOT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NOT NULL COMMENT '最后更新时间',
  `delete_tag` bit(1) NULL DEFAULT b'0' COMMENT '删除标记',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;
