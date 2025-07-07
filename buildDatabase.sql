create database ssefish;
use ssefish;
create table users
(
    id       INT AUTO_INCREMENT PRIMARY KEY,                            -- 用户ID，自增主键
    name     VARCHAR(255) NOT NULL,                                     -- 用户名
    password VARCHAR(255) NOT NULL,                                     -- 密码
    email    VARCHAR(255) NOT NULL UNIQUE,                              -- 邮箱，唯一
    wantList varchar(255),                                              -- 想要的商品ID列表，存储为JSON格式
    picture  VARCHAR(255) DEFAULT 'https://img.icons8.com/ios/452/user' -- 头像路径或URL
);

create table products
(
    id          INT AUTO_INCREMENT PRIMARY KEY,                                   -- 商品ID，自增主键
    name        VARCHAR(255)   NOT NULL,                                          -- 商品名称
    price       DECIMAL(10, 2) NOT NULL,                                          -- 商品价格，支持两位小数
    image       VARCHAR(255),                                                     -- 商品图片路径或URL
    description VARCHAR(255),                                                     -- 商品描述
    address     INT,                                                              -- 地址
    buyerId     INT,                                                              -- 买家ID
    sellerId    INT            NOT NULL DEFAULT 1,                                -- 卖家ID
    `condition` TINYINT        NOT NULL DEFAULT 1 CHECK (`condition` IN (1, 2)),  -- 商品成色：二手(1)、全新(2)
    status      TINYINT        NOT NULL DEFAULT 1 CHECK (status IN (1, 2, 3, 4)), -- 状态：待支付(1)、已支付(2)、已发货(3)、已完成(4)
    createTime  TIMESTAMP               DEFAULT CURRENT_TIMESTAMP,                -- 创建时间，默认当前时间
    likes       INT                     DEFAULT 0                                 -- 想要人数，默认0
);

create table trades
(
    id          INT AUTO_INCREMENT PRIMARY KEY,                      -- 交易ID，自增主键
    buyerId     INT            NOT NULL DEFAULT 1,                   -- 买家ID
    sellerId    INT            NOT NULL DEFAULT 1,                   -- 卖家ID
    productId   INT            NOT NULL DEFAULT 1,                   -- 商品ID
    productName VARCHAR(255)   NOT NULL,                             -- 商品名称
    totalAmount DECIMAL(10, 2) NOT NULL,                             -- 总金额
    status      TINYINT        NOT NULL CHECK (status IN (1, 2, 3)), -- 订单状态：进行中(1)、已完成(2)、取消(3)
    createTime  TIMESTAMP               DEFAULT CURRENT_TIMESTAMP    -- 交易创建时间
);

create table announcements
(
    id          INT AUTO_INCREMENT PRIMARY KEY,                      -- 通知ID，自增主键
    title       VARCHAR(255)   NOT NULL,                             -- 通知标题
    content     VARCHAR(255)   NOT NULL,                             -- 通知内容
    createTime  TIMESTAMP               DEFAULT CURRENT_TIMESTAMP    -- 通知创建时间
);

insert into users (id, name, password, email, wantList, picture)
values (1, 'admin', 'admin', 'admin@admin.com', null, 'https://img.icons8.com/ios/452/user');
