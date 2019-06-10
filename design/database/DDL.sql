-- pin_mini_program_form_id_record: table
CREATE TABLE `pin_mini_program_form_id_record`
(
  `id`             int(11) unsigned NOT NULL COMMENT 'Log ID',
  `wechat_user_id` int(11) unsigned NOT NULL COMMENT '微信用户ID',
  `form_id`        char(255)        NOT NULL COMMENT '模板名',
  `create_time`    timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `pin_mini_program_form_id_record_pin_wechat_user_id_fk` (`wechat_user_id`),
  CONSTRAINT `pin_mini_program_form_id_record_pin_wechat_user_id_fk` FOREIGN KEY (`wechat_user_id`) REFERENCES `pin_wechat_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='微信消息推送提权 ID 记录';

-- No native definition for element: pin_mini_program_form_id_record_pin_wechat_user_id_fk (index)

-- pin_mini_program_message_template: table
CREATE TABLE `pin_mini_program_message_template`
(
  `id`           int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '模板id',
  `template_key` char(50)         NOT NULL COMMENT '模板编号',
  `name`         char(100)        NOT NULL COMMENT '模板名',
  `content`      varchar(1000)    NOT NULL COMMENT '回复内容',
  `create_time`  varchar(15)      NOT NULL COMMENT '添加时间',
  `status`       tinyint(4)       NOT NULL DEFAULT '0' COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC COMMENT ='微信消息模板';

-- pin_order_group: table
CREATE TABLE `pin_order_group`
(
  `id`                         int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '拼团单ID',
  `owner_user_id`              int(11) unsigned NOT NULL,
  `store_id`                   int(11) unsigned          DEFAULT NULL,
  `status`                     tinyint(1)       NOT NULL COMMENT '0: 正在拼团；1：已结束拼团',
  `create_time`                timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `close_time`                 timestamp        NULL     DEFAULT NULL,
  `actual_finish_time`         timestamp        NULL     DEFAULT NULL,
  `total_amount_of_money_paid` decimal(9, 2)             DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `pin_order_group_pin_store_id_fk` (`store_id`),
  KEY `pin_order_group_pin_user_id_fk` (`owner_user_id`),
  CONSTRAINT `pin_order_group_pin_store_id_fk` FOREIGN KEY (`store_id`) REFERENCES `pin_store` (`id`),
  CONSTRAINT `pin_order_group_pin_user_id_fk` FOREIGN KEY (`owner_user_id`) REFERENCES `pin_user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='团体拼单订单号';

-- No native definition for element: pin_order_group_pin_user_id_fk (index)

-- No native definition for element: pin_order_group_pin_store_id_fk (index)

-- pin_order_individual: table
CREATE TABLE `pin_order_individual`
(
  `id`                    int(11) unsigned       NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_group_id`        int(11) unsigned       NOT NULL COMMENT '团订单号',
  `store_id`              int(11) unsigned       NOT NULL COMMENT '商户ID',
  `user_id`               int(11) unsigned       NOT NULL COMMENT '用户id',
  `receiver_name`         varchar(32)            NOT NULL COMMENT '用户姓名',
  `receiver_phone`        varchar(24)            NOT NULL COMMENT '用户电话',
  `delivery_address`      varchar(512)           NOT NULL COMMENT '详细地址',
  `total_product_number`  int(11) unsigned       NOT NULL DEFAULT '0' COMMENT '订单商品总数',
  `total_price`           decimal(8, 2) unsigned NOT NULL DEFAULT '0.00' COMMENT '订单总价',
  `shippingFee`               decimal(8, 2) unsigned NOT NULL DEFAULT '0.00' COMMENT '邮费',
  `pay_price`             decimal(8, 2) unsigned NOT NULL DEFAULT '0.00' COMMENT '实际支付金额',
  `pay_postage`           decimal(8, 2) unsigned NOT NULL DEFAULT '0.00' COMMENT '支付邮费',
  `balance_paid_price`    decimal(8, 2) unsigned NOT NULL DEFAULT '0.00' COMMENT '余额支付的金额部分',
  `pay_time`              timestamp              NULL     DEFAULT NULL COMMENT '支付时间',
  `paid`                  tinyint(1) unsigned    NOT NULL DEFAULT '0' COMMENT '支付状态',
  `pay_type`              varchar(32)            NOT NULL COMMENT '支付方式; BALANCE 余额 / WECHAT 微信',
  `create_time`           timestamp              NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `status`                tinyint(1)             NOT NULL DEFAULT '0' COMMENT '订单状态（-1 : 申请退款中 -2 : 退货成功 0：待发货；1：待收货；2：已收货；3：待评价; 4: 已评价）',
  `refund_status`         tinyint(1) unsigned    NOT NULL DEFAULT '0' COMMENT '0 未退款 1 申请中 2 已退款',
  `refund_reason_image`   varchar(255)                    DEFAULT NULL COMMENT '退款图片',
  `refund_reason_explain` varchar(255)                    DEFAULT NULL COMMENT '退款用户说明',
  `refund_apply_time`     timestamp              NULL     DEFAULT NULL COMMENT '退款申请时间',
  `refund_refuse_reason`  varchar(255)                    DEFAULT NULL COMMENT '商户填写的不退款的理由',
  `refund_price`          decimal(8, 2) unsigned NOT NULL DEFAULT '0.00' COMMENT '退款金额',
  `delivery_name`         varchar(64)                     DEFAULT NULL COMMENT '快递名称/送货人姓名',
  `delivery_type`         varchar(32)                     DEFAULT NULL COMMENT '发货类型',
  `delivery_id`           varchar(64)                     DEFAULT NULL COMMENT '快递单号/手机号',
  `gained_credit`         int(11) unsigned       NOT NULL DEFAULT '0' COMMENT '消费赚取积分',
  `merchant_remark`       varchar(512)           NOT NULL COMMENT '商户备注',
  `user_remark`           varchar(512)                    DEFAULT NULL COMMENT '管理员备注',
  `is_group`              tinyint(1) unsigned    NOT NULL DEFAULT '0' COMMENT '是否拼团',
  `total_cost`            decimal(8, 2) unsigned NOT NULL COMMENT '总成本价',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `pin_order_individual_pin_store_id_fk` (`store_id`),
  KEY `pin_order_individual_pin_user_id_fk` (`user_id`),
  CONSTRAINT `pin_order_individual_pin_store_id_fk` FOREIGN KEY (`store_id`) REFERENCES `pin_store` (`id`),
  CONSTRAINT `pin_order_individual_pin_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `pin_user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='订单表';

-- No native definition for element: pin_order_individual_pin_store_id_fk (index)

-- No native definition for element: pin_order_individual_pin_user_id_fk (index)

-- pin_order_item: table
CREATE TABLE `pin_order_item`
(
  `id`                  int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '订单已选品ID',
  `amount`              int(11) unsigned NOT NULL COMMENT '已选品数量',
  `total_price`         decimal(8, 2)    NOT NULL COMMENT '商品总价',
  `total_cost`          decimal(8, 2)    NOT NULL COMMENT '商品总成本',
  `order_individual_id` int(11)                   DEFAULT NULL COMMENT '个人订单id',
  `is_submitted`        tinyint(1)       NOT NULL DEFAULT '0' COMMENT '是否已提交结算',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='已选商品表';

-- pin_product: table
CREATE TABLE `pin_product`
(
  `id`                    int(11) unsigned       NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `store_id`              int(11) unsigned       NOT NULL COMMENT '商户Id(0为总后台管理员创建,不为0的时候是商户后台创建)',
  `category_id`           int(11) unsigned       NOT NULL COMMENT '分类id',
  `image_urls`            varchar(512)           NOT NULL COMMENT '商品图片（可以多个）',
  `name`                  varchar(128)           NOT NULL COMMENT '商品名称',
  `info`                  varchar(256)           NOT NULL COMMENT '商品简介',
  `keyword`               varchar(256)           NOT NULL COMMENT '关键字',
  `changedAmount`                 decimal(8, 2) unsigned NOT NULL DEFAULT '0.00' COMMENT '商品价格',
  `price_before_discount` decimal(8, 2) unsigned          DEFAULT '0.00' COMMENT '折扣前原价（前端显示为灰色横线划掉的样式）',
  `unit_name`             varchar(32)            NOT NULL COMMENT '单位名',
  `stock_count`           int(11) unsigned       NOT NULL DEFAULT '0' COMMENT '库存',
  `sold_count`            int(11) unsigned       NOT NULL DEFAULT '0' COMMENT '销量',
  `is_shown`              tinyint(1)             NOT NULL DEFAULT '1' COMMENT '状态（0：未上架，1：上架）',
  `is_hot`                tinyint(1)             NOT NULL DEFAULT '0' COMMENT '是否热卖',
  `is_new`                tinyint(1)             NOT NULL DEFAULT '0' COMMENT '是否新品',
  `shipping_fee`          decimal(8, 2) unsigned NOT NULL DEFAULT '0.00' COMMENT '邮费',
  `is_free_shipping`      tinyint(1) unsigned    NOT NULL DEFAULT '0' COMMENT '是否包邮：0: 否；1：是',
  `description`           text                   NOT NULL COMMENT '产品描述',
  `create_time`           timestamp              NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `credit_to_give`        int(11) unsigned       NOT NULL DEFAULT '0' COMMENT '购买将获得的积分',
  `cost`                  decimal(8, 2) unsigned NOT NULL COMMENT '成本价',
  `visit_count`           int(11)                         DEFAULT '0' COMMENT '浏览量',
  PRIMARY KEY (`id`),
  KEY `pin_product_pin_store_id_fk` (`store_id`),
  KEY `pin_product_pin_settings_product_category_id_fk` (`category_id`),
  CONSTRAINT `pin_product_pin_settings_product_category_id_fk` FOREIGN KEY (`category_id`) REFERENCES `pin_settings_product_category` (`id`),
  CONSTRAINT `pin_product_pin_store_id_fk` FOREIGN KEY (`store_id`) REFERENCES `pin_store` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='商品表';

-- No native definition for element: pin_product_pin_store_id_fk (index)

-- No native definition for element: pin_product_pin_settings_product_category_id_fk (index)

-- pin_product_attribute_definition: table
CREATE TABLE `pin_product_attribute_definition`
(
  `id`               int(11) unsigned NOT NULL AUTO_INCREMENT,
  `product_id`       int(11) unsigned NOT NULL,
  `attribute_name`   varchar(32)      NOT NULL COMMENT '属性类别名',
  `attribute_values` varchar(256)     NOT NULL COMMENT '属性类别值（多个用逗号分隔）',
  PRIMARY KEY (`id`),
  KEY `pin_product_attribute_definition_pin_product_id_fk` (`product_id`),
  CONSTRAINT `pin_product_attribute_definition_pin_product_id_fk` FOREIGN KEY (`product_id`) REFERENCES `pin_product` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='产品属性定义';

-- No native definition for element: pin_product_attribute_definition_pin_product_id_fk (index)

-- pin_settings_constant: table
CREATE TABLE `pin_settings_constant`
(
  `constant_key`   varchar(16) NOT NULL,
  `constant_value` varchar(16) NOT NULL,
  PRIMARY KEY (`constant_key`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='设置常量表';

-- pin_settings_express: table
CREATE TABLE `pin_settings_express`
(
  `id`           int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '快递公司id',
  `code`         varchar(24)      NOT NULL COMMENT '快递公司搜索代码',
  `name`         varchar(24)      NOT NULL COMMENT '快递公司名',
  `sort`         int(11)          NOT NULL COMMENT '排序',
  `is_activated` tinyint(1)       NOT NULL COMMENT '是否激活（是否向商家显示）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 426
  DEFAULT CHARSET = utf8mb4 COMMENT ='快递公司表';

-- pin_settings_product_category: table
CREATE TABLE `pin_settings_product_category`
(
  `id`                 int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '商品分类表ID',
  `parent_category_id` int(11) unsigned NOT NULL COMMENT '父id',
  `category_name`      varchar(100)     NOT NULL COMMENT '分类名称',
  `sort`               mediumint(11)    NOT NULL COMMENT '排序',
  `image_url`          varchar(128)     NOT NULL DEFAULT '' COMMENT '图标',
  `is_activated`       tinyint(1)       NOT NULL DEFAULT '1' COMMENT '是否激活',
  `create_time`        timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='商品分类表';

-- pin_settings_store_category: table
CREATE TABLE `pin_settings_store_category`
(
  `id`            int(11) unsigned NOT NULL AUTO_INCREMENT,
  `category_name` varchar(24)      NOT NULL COMMENT '主营商品类别',
  `image_url`     varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='商户主营商品类别表';

-- pin_store: table
CREATE TABLE `pin_store`
(
  `id`            int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '店铺id',
  `name`          varchar(32)      NOT NULL COMMENT '店铺名称',
  `description`   varchar(255)     NOT NULL COMMENT '店铺描述',
  `phone`         varchar(24)           DEFAULT NULL COMMENT '店主电话号',
  `email`         varchar(64)           DEFAULT NULL COMMENT '店主电子邮件',
  `create_time`   timestamp        NULL DEFAULT NULL COMMENT '店铺创建时间',
  `logo_url`      varchar(255)          DEFAULT NULL COMMENT '店铺logo链接',
  `owner_user_id` int(11) unsigned NOT NULL COMMENT '店铺创建者用户id',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='店铺基础信息表';

-- pin_system_admin: table
CREATE TABLE `pin_system_admin`
(
  `id`              int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '后台管理员表ID',
  `password_hash`   varchar(64)      NOT NULL COMMENT '后台管理员密码',
  `username`        varchar(16)      NOT NULL COMMENT '后台管理员姓名',
  `role`            int(11) unsigned NOT NULL COMMENT '后台管理员权限',
  `last_login_time` timestamp        NULL DEFAULT NULL,
  `create_time`     timestamp        NULL DEFAULT NULL,
  `last_login_ip`   varchar(32)           DEFAULT NULL COMMENT '上次登录IP',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统超级管理员登录凭据表';

-- pin_user: table
CREATE TABLE `pin_user`
(
  `id`                     int(11) unsigned NOT NULL AUTO_INCREMENT,
  `phone`                  varchar(24)               DEFAULT NULL COMMENT '电话号',
  `email`                  varchar(64)               DEFAULT NULL,
  `password_hash`          varchar(128)              DEFAULT NULL,
  `create_time`            timestamp        NULL     DEFAULT NULL,
  `last_login_time`        timestamp        NULL     DEFAULT NULL,
  `last_password_edit_time` timestamp        NULL     DEFAULT NULL,
  `avatar_url`             varchar(255)              DEFAULT NULL COMMENT '头像链接（若微信有则复制微信的数据）',
  `nickname`               varchar(24)      NOT NULL,
  `balance`                decimal(9, 2)    NOT NULL DEFAULT '0.00',
  `last_login_ip`          varchar(64)               DEFAULT NULL,
  `create_ip`              varchar(64)               DEFAULT NULL,
  `gender`                 tinyint(1)                DEFAULT NULL COMMENT '1: 男；2：女；0:未知',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户基础信息表';

-- pin_user_address: table
CREATE TABLE `pin_user_address`
(
  `id`          int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户地址id',
  `user_id`     int(11) unsigned NOT NULL COMMENT '用户id',
  `real_name`   varchar(32)      NOT NULL COMMENT '收货人姓名',
  `phone`       varchar(16)      NOT NULL COMMENT '收货人电话',
  `province`    varchar(64)      NOT NULL COMMENT '收货人所在省',
  `city`        varchar(64)      NOT NULL COMMENT '收货人所在市',
  `district`    varchar(64)      NOT NULL COMMENT '收货人所在区或县',
  `detail`      varchar(256)     NOT NULL COMMENT '收货人详细地址',
  `post_code`   int(10)          NOT NULL COMMENT '邮编',
  `longitude`   varchar(16)      NOT NULL DEFAULT '0' COMMENT '经度',
  `latitude`    varchar(16)      NOT NULL DEFAULT '0' COMMENT '纬度',
  `create_time` timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `pin_user_address_pin_user_id_fk` (`user_id`),
  CONSTRAINT `pin_user_address_pin_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `pin_user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- No native definition for element: pin_user_address_pin_user_id_fk (index)

-- pin_user_balance_record: table
CREATE TABLE `pin_user_balance_record`
(
  `id`                  int(11) unsigned NOT NULL,
  `user_id`             int(11) unsigned      DEFAULT NULL COMMENT '充值用户id',
  `order_individual_id` int(11) unsigned      DEFAULT NULL COMMENT '个人订单号',
  `changedAmount`               decimal(8, 2)         DEFAULT NULL COMMENT '动账金额',
  `type`                tinyint(1)            DEFAULT NULL COMMENT '类型, 0: 返现, 1: 抵扣; 2: 充值',
  `create_time`         timestamp        NULL DEFAULT NULL COMMENT '动账时间',
  PRIMARY KEY (`id`),
  KEY `pin_user_balance_record_pin_user_id_fk` (`user_id`),
  CONSTRAINT `pin_user_balance_record_pin_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `pin_user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户余额动账记录';

-- No native definition for element: pin_user_balance_record_pin_user_id_fk (index)

-- pin_user_notification: table
CREATE TABLE `pin_user_notification`
(
  `id`          int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id`     int(11) unsigned NOT NULL COMMENT '接收消息的用户id',
  `type`        tinyint(1)       NOT NULL DEFAULT '1' COMMENT '消息通知类型（1：系统消息；2：用户通知）',
  `sender`      varchar(24)      NOT NULL COMMENT '发送人',
  `title`       varchar(24)      NOT NULL COMMENT '通知消息的标题信息',
  `content`     varchar(255)     NOT NULL COMMENT '通知消息的内容',
  `create_time` timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '通知消息发送的时间',
  `send_time`   timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  PRIMARY KEY (`id`),
  KEY `pin_user_notification_pin_user_id_fk` (`user_id`),
  CONSTRAINT `pin_user_notification_pin_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `pin_user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户通知表';

-- No native definition for element: pin_user_notification_pin_user_id_fk (index)

-- pin_user_product_collection: table
CREATE TABLE `pin_user_product_collection`
(
  `id`          int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id`     int(11) unsigned          DEFAULT NULL COMMENT '用户ID',
  `product_id`  int(11) unsigned NOT NULL COMMENT '商品ID',
  `create_time` timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `pin_user_product_collection_pin_product_id_fk` (`product_id`),
  KEY `pin_user_product_collection_pin_user_id_fk` (`user_id`),
  CONSTRAINT `pin_user_product_collection_pin_product_id_fk` FOREIGN KEY (`product_id`) REFERENCES `pin_product` (`id`),
  CONSTRAINT `pin_user_product_collection_pin_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `pin_user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户收藏商品表';

-- No native definition for element: pin_user_product_collection_pin_user_id_fk (index)

-- No native definition for element: pin_user_product_collection_pin_product_id_fk (index)

-- pin_user_product_comment: table
CREATE TABLE `pin_user_product_comment`
(
  `id`                       int(11) unsigned    NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `user_id`                  int(11) unsigned    NOT NULL COMMENT '用户ID',
  `order_individual_id`      int(11) unsigned    NOT NULL COMMENT '订单ID',
  `product_id`               int(11) unsigned    NOT NULL COMMENT '产品ID',
  `grade`                    tinyint(1)                   DEFAULT NULL COMMENT '0: 好评；1：中评；2：差评',
  `product_score`            tinyint(1)          NOT NULL COMMENT '商品打分',
  `service_score`            tinyint(1)          NOT NULL COMMENT '服务打分',
  `content`                  varchar(512)        NOT NULL COMMENT '评论内容',
  `images_urls`              text                NOT NULL COMMENT '评论图片',
  `create_time`              timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '评论时间',
  `merchant_comment_content` varchar(255)                 DEFAULT NULL COMMENT '商家回复内容',
  `merchant_comment_time`    timestamp           NULL     DEFAULT NULL COMMENT '商家回复时间',
  `is_deleted`               tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0未删除；1已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `order_individual_id_index` (`order_individual_id`) USING BTREE,
  KEY `pin_user_product_comment_pin_product_id_fk` (`product_id`),
  KEY `pin_user_product_comment_pin_user_id_fk` (`user_id`),
  CONSTRAINT `pin_user_product_comment_pin_order_individual_id_fk` FOREIGN KEY (`order_individual_id`) REFERENCES `pin_order_individual` (`id`),
  CONSTRAINT `pin_user_product_comment_pin_product_id_fk` FOREIGN KEY (`product_id`) REFERENCES `pin_product` (`id`),
  CONSTRAINT `pin_user_product_comment_pin_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `pin_user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户对商品的评论表';

-- No native definition for element: pin_user_product_comment_pin_user_id_fk (index)

-- No native definition for element: pin_user_product_comment_pin_product_id_fk (index)

-- pin_user_product_visit_record: table
CREATE TABLE `pin_user_product_visit_record`
(
  `id`         int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id`    int(11) unsigned NOT NULL,
  `product_id` int(11) unsigned NOT NULL,
  `visit_time` timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `visit_ip`   varchar(32)               DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `pin_user_product_visit_record_pin_product_id_fk` (`product_id`),
  KEY `pin_user_product_visit_record_pin_user_id_fk` (`user_id`),
  CONSTRAINT `pin_user_product_visit_record_pin_product_id_fk` FOREIGN KEY (`product_id`) REFERENCES `pin_product` (`id`),
  CONSTRAINT `pin_user_product_visit_record_pin_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `pin_user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户商品访问记录';

-- No native definition for element: pin_user_product_visit_record_pin_user_id_fk (index)

-- No native definition for element: pin_user_product_visit_record_pin_product_id_fk (index)

-- pin_user_store_collection: table
CREATE TABLE `pin_user_store_collection`
(
  `id`          int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id`     int(11) unsigned          DEFAULT NULL COMMENT '用户ID',
  `store_id`    int(11) unsigned NOT NULL COMMENT '店铺ID',
  `create_time` timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `pin_user_store_collection_pin_store_id_fk` (`store_id`),
  KEY `pin_user_store_collection_pin_user_id_fk` (`user_id`),
  CONSTRAINT `pin_user_store_collection_pin_store_id_fk` FOREIGN KEY (`store_id`) REFERENCES `pin_store` (`id`),
  CONSTRAINT `pin_user_store_collection_pin_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `pin_user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户收藏店铺表';

-- No native definition for element: pin_user_store_collection_pin_user_id_fk (index)

-- No native definition for element: pin_user_store_collection_pin_store_id_fk (index)

-- pin_wechat_user: table
CREATE TABLE `pin_wechat_user`
(
  `id`          int(11) unsigned NOT NULL COMMENT '微信用户id',
  `user_id`     int(11) unsigned NOT NULL COMMENT '映射的用户ID',
  `union_id`    varchar(30)           DEFAULT NULL COMMENT '只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段',
  `open_id`     varchar(30)           DEFAULT NULL COMMENT '用户的标识 对当前公众号唯一',
  `nickname`    varchar(64)           DEFAULT NULL COMMENT '用户的昵称',
  `avatar_url`  varchar(256)          DEFAULT NULL COMMENT '用户头像',
  `gender`      tinyint(1)            DEFAULT '0' COMMENT '用户的性别，值为1时是男性，值为2时是女性，值为0时是未知',
  `city`        varchar(64)           DEFAULT NULL COMMENT '用户所在城市',
  `language`    varchar(16)           DEFAULT NULL COMMENT '用户的语言，简体中文为zh_CN',
  `province`    varchar(64)           DEFAULT NULL COMMENT '用户所在国家',
  `country`     varchar(64)           DEFAULT NULL COMMENT '用户所在省份',
  `create_time` timestamp        NULL DEFAULT NULL COMMENT '创建时间',
  `session_key` varchar(32)           DEFAULT NULL COMMENT '小程序用户会话密匙',
  PRIMARY KEY (`id`),
  KEY `pin_wechat_user_pin_user_id_fk` (`user_id`),
  CONSTRAINT `pin_wechat_user_pin_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `pin_user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- No native definition for element: pin_wechat_user_pin_user_id_fk (index)