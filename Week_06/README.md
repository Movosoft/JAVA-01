# 第六周作业

## 第十一课 Java相关框架(3)

**1.（选做）**尝试使用 Lambda/Stream/Guava 优化之前作业的代码。

**2.（选做）**尝试使用 Lambda/Stream/Guava 优化工作中编码的代码。

**3.（选做）**根据课上提供的材料，系统性学习一遍设计模式，并在工作学习中思考如何用设计模式解决问题。

**4.（选做）**根据课上提供的材料，深入了解 Google 和 Alibaba 编码规范，并根据这些规范，检查自己写代码是否符合规范，有什么可以改进的。

## 第十二课 性能与SQL优化(1)

**1.（选做）**基于课程中的设计原则和最佳实践，分析是否可以将自己负责的业务系统进行数据库设计或是数据库服务器方面的优化。

**2.（必做）**基于电商交易场景（用户、商品、订单），设计一套简单的表结构，提交 DDL 的 SQL 文件到 Github（后面 2 周的作业依然要是用到这个表结构）。

```
CREATE SCHEMA `online_retailer` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

- 用户表
用户id
用户名
密码
昵称
身份证号
手机号
最后登录时间
创建时间
最后更新时间
删除标记

create table `dt_user` (
  `user_id` int(11) not null auto_increment comment '用户id',
  `username` varchar(20) character set utf8mb4 collate utf8mb4_bin not null comment '用户名',
  `password` varchar(50) character set utf8mb4 collate utf8mb4_bin not null comment '密码',
  `nickname` varchar(10) character set utf8mb4 collate utf8mb4_bin default null comment '昵称',
  `id_card_no` varchar(18) character set utf8mb4 collate utf8mb4_bin default null comment '身份称号吗',
  `mobile` varchar(13) character set utf8mb4 collate utf8mb4_bin default null comment '手机号码',
  `last_login_time` timestamp null default null comment '最后登陆时间',
  `create_time` timestamp not null comment '创建时间',
  `last_update_time` timestamp not null comment '最后更新时间',
  `delete_tag` bit(1) default b'0' comment '删除标记',
  primary key (`user_id`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_bin;

- 收货地址表
收货地址id
买家id(用户表id)
收货地址

create table `dt_shipping_address` (
  `sa_id` int(11) not null auto_increment comment '收货地址id',
  `buyer_id` int(11) not null comment '买家id',
  `address` varchar(50) character set utf8mb4 collate utf8mb4_bin not null comment '收货地址',
  primary key (`sa_id`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_bin;

- 数据字典类别表
数据字典类别id
数据字典类别名称
数据字典类别排序号

create table `dt_dictionary_category` (
  `dictionary_category_id` int(11) not null auto_increment comment '数据字典类别id',
  `dictionary_category_name` varchar(50) collate utf8mb4_bin not null comment '数据字典类别名称',
  `dictionary_category_order` int(11) not null comment '数据字典类别排序号',
  primary key (`dictionary_category_id`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_bin;

- 数据字典表
数据字典id
数据字典类别id
数据字典名称
数据字典排序号

create table `dt_dictionary` (
  `dictionary_id` int(11) not null auto_increment comment '数据字典id',
  `dictionary_category_id` int(11) not null comment '数据字典类别id',
  `dictionary_name` varchar(50) collate utf8mb4_bin not null comment '数据字典名称',
  `dictionary_order` int(11) not null comment '数据字典排序号',
  primary key (`dictionary_id`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_bin;

- 静态资源表
静态资源id
静态资源类别(数据字典id)
静态资源描述
静态资源对应位置

create table `dt_static_resource` (
  `sr_id` bigint(20) not null auto_increment comment '静态资源id',
  `sr_category` int(11) not null comment '静态资源类别',
  `sr_desc` varchar(50) collate utf8mb4_bin default null comment '静态资源描述',
  `sr_path` varchar(200) collate utf8mb4_bin not null comment '静态资源对应位置',
  primary key (`sr_id`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_bin;

- 商品表
商品id
商品编码
商品版本
商品名称
商品类别(数据字典id)
商品单价整数位
商品单价小数位
商品描述
商品图片(静态资源id)
商品版本
创建时间
最后更新时间
删除标记

create table `dt_wares` (
  `wares_id` bigint(20) not null auto_increment comment '商品id',
  `wares_code` varchar(10) collate utf8mb4_bin not null comment '商品编码',
  `wares_version` int(11) not null comment '商品版本',
  `wares_name` varchar(10) collate utf8mb4_bin not null comment '商品名称',
  `wares_category` int(11) not null comment '商品类别',
  `wares_price_i` int(11) not null comment '商品单价整数位',
  `wares_price_d` int(11) not null comment '商品单价小数位',
  `wares_desc` varchar(20) collate utf8mb4_bin default null comment '商品描述',
  `wares_img` bigint(20) default null comment '商品图片',
  `create_time` timestamp not null comment '创建时间',
  `last_update_time` timestamp not null comment '最后更新时间',
  `delete_tag` bit(1) default b'0' comment '删除标记',
  primary key (`wares_id`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_bin;

- 库存表
库存id
商品id
库存数量
库存状态：可售、下架(数据字典id)
创建时间
最后更新时间
删除标记

create table `dt_stock` (
  `stock_id` int(11) not null auto_increment comment '库存id',
  `wares_id` bigint(20) not null comment '商品id',
  `stock_num` int(11) not null comment '库存数量',
  `stock_status` int(11) not null comment '库存状态',
  `create_time` timestamp not null comment '创建时间',
  `last_update_time` timestamp not null comment '最后更新时间',
  `delete_tag` bit(1) default b'0' comment '删除标记',
  primary key (`stock_id`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_bin;

- 购物车表
购物车id
买家id
商品id
商品数量
创建时间
最后更新时间
删除标记

create table `dt_shopping_cart` (
  `sc_id` int(11) not null auto_increment comment '购物车id',
  `buyer_id` int(11) not null comment '买家id',
  `wares_id` bigint(20) not null comment '商品id',
  `wares_num` int(11) not null comment '商品数量',
  `create_time` timestamp not null comment '创建时间',
  `last_update_time` timestamp not null comment '最后更新时间',
  `delete_tag` bit(1) default b'0' comment '删除标记',
  primary key (`sc_id`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_bin;

- 订单表
订单id
订单号
买家id
订单状态(数据字典id)
订单金额整数位
订单金额小数位
实付金额整数位
实付金额小数位
支付完成时间
订单完成时间
创建时间
最后更新时间
删除标记

create table `dt_order`  (
  `order_id` bigint(20) not null auto_increment comment '订单id',
  `order_code` varchar(20) character set utf8mb4 collate utf8mb4_bin not null comment '订单号',
  `buyer_id` int(11) not null comment '买家id',
  `order_status` int(11) not null comment '订单状态',
  `order_price_i` int(11) not null comment '订单金额整数位',
  `order_price_d` int(11) not null comment '订单金额小数位',
  `pay_price_i` int(11) null default null comment '实付金额整数位',
  `pay_price_d` int(11) null default null comment '实付金额小数位',
  `pay_complete_time` timestamp(0) null default null comment '支付完成时间',
  `order_complete_time` timestamp(0) null default null comment '订单完成时间',
  `create_time` timestamp(0) not null comment '创建时间',
  `last_update_time` timestamp(0) not null comment '最后更新时间',
  `delete_tag` bit(1) null default b'0' comment '删除标记',
  primary key (`order_id`)
) engine = innodb character set = utf8mb4 collate = utf8mb4_bin;

-订单明细表
订单明细id
订单号
商品id
订单商品数量
实付金额整数位
实付金额小数位
创建时间
最后更新时间
删除标记

create table `dt_order_detail`  (
  `order_detail_id` bigint(20) not null auto_increment comment '订单明细id',
  `order_code` varchar(20) character set utf8mb4 collate utf8mb4_bin not null comment '订单号',
  `wares_id` bigint(20) not null comment '商品id',
  `wares_num` int(11) not null comment '订单商品数量',
  `pay_price_i` int(11) null default null comment '实付金额整数位',
  `pay_price_d` int(11) null default null comment '实付金额小数位',
  `create_time` timestamp(0) not null comment '创建时间',
  `last_update_time` timestamp(0) not null comment '最后更新时间',
  `delete_tag` bit(1) null default b'0' comment '删除标记',
  primary key (`order_detail_id`)
) engine = innodb character set = utf8mb4 collate = utf8mb4_bin; 

```

**3.（选做）**尽可能多的从“常见关系数据库”中列的清单，安装运行，并使用上一题的 SQL 测试简单的增删改查。

**4.（选做）**基于上一题，尝试对各个数据库测试 100 万订单数据的增删改查性能。

**5.（选做**）尝试对 MySQL 不同引擎下测试 100 万订单数据的增删改查性能。

**6.（选做）**模拟 1000 万订单数据，测试不同方式下导入导出（数据备份还原）MySQL 的速度，包括 jdbc 程序处理和命令行处理。思考和实践，如何提升处理效率。

**7.（选做）**对 MySQL 配置不同的数据库连接池（DBCP、C3P0、Druid、Hikari），测试增删改查 100 万次，对比性能，生成报告。