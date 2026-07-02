-- ============================================
-- 简易购物网站 - 数据库初始化脚本
-- 使用方法：在 MySQL 中执行 source sql/shop.sql
-- ============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS shopping DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE shopping;

-- ============================================
-- 用户表
-- ============================================
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
    `id`          INT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    `username`    VARCHAR(50)  NOT NULL UNIQUE COMMENT '用户名',
    `password`    VARCHAR(100) NOT NULL       COMMENT '密码（MD5加密）',
    `email`       VARCHAR(100) DEFAULT NULL   COMMENT '邮箱',
    `phone`       VARCHAR(20)  DEFAULT NULL   COMMENT '电话',
    `address`     VARCHAR(255) DEFAULT NULL   COMMENT '地址',
    `role`        VARCHAR(20)  DEFAULT 'user' COMMENT '角色：user-普通用户，admin-管理员',
    `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 插入默认管理员和测试用户
INSERT INTO `users` (`username`, `password`, `email`, `role`) VALUES
('admin', MD5('admin123'), 'admin@shop.com', 'admin'),
('test',  MD5('test123'),  'test@shop.com',  'user');

-- ============================================
-- 商品分类表
-- ============================================
DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories` (
    `id`          INT AUTO_INCREMENT PRIMARY KEY COMMENT '分类ID',
    `name`        VARCHAR(50)  NOT NULL COMMENT '分类名称',
    `sort`        INT          DEFAULT 0 COMMENT '排序',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '分类描述'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

INSERT INTO `categories` (`name`, `sort`) VALUES
('电子产品', 1),
('服装配饰', 2),
('图书教材', 3),
('家居生活', 4),
('零食食品', 5);

-- ============================================
-- 商品表
-- ============================================
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products` (
    `id`          INT AUTO_INCREMENT PRIMARY KEY COMMENT '商品ID',
    `name`        VARCHAR(100)   NOT NULL     COMMENT '商品名称',
    `description` TEXT                        COMMENT '商品描述',
    `price`       DECIMAL(10,2)  NOT NULL     COMMENT '价格',
    `stock`       INT            DEFAULT 0    COMMENT '库存',
    `image`       VARCHAR(255)   DEFAULT NULL COMMENT '图片路径',
    `category_id` INT            DEFAULT NULL COMMENT '分类ID',
    `create_time` DATETIME       DEFAULT CURRENT_TIMESTAMP COMMENT '上架时间',
    FOREIGN KEY (`category_id`) REFERENCES `categories`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 插入演示商品
INSERT INTO `products` (`name`, `description`, `price`, `stock`, `image`, `category_id`) VALUES
('机械键盘 青轴RGB', '87键有线机械键盘，青轴手感，RGB背光，电竞游戏办公通用', 299.00, 50, 'keyboard.jpg', 1),
('无线蓝牙耳机', '降噪蓝牙5.3耳机，续航30小时，入耳式舒适佩戴', 159.00, 100, 'earphone.jpg', 1),
('Type-C数据线 快充', '100W超级快充数据线，1.5米编织线，兼容苹果安卓', 29.90, 200, 'cable.jpg', 1),
('简约纯棉T恤', '男女同款纯棉圆领短袖T恤，多色可选，舒适透气', 79.00, 150, 'tshirt.jpg', 2),
('牛仔裤 修身款', '经典修身牛仔裤，弹力面料，百搭潮流', 199.00, 80, 'jeans.jpg', 2),
('Java编程思想（第4版）', 'Java经典入门教材，从入门到精通，附赠源码', 69.00, 60, 'java_book.jpg', 3),
('算法导论（第3版）', '计算机算法领域经典之作，程序员面试必备', 89.00, 45, 'algorithm.jpg', 3),
('简约台灯 LED护眼', '三档调光护眼台灯，无频闪，USB充电，宿舍书房通用', 129.00, 70, 'lamp.jpg', 4),
('保温杯 500ml', '304不锈钢保温杯，12小时保温，真空断热', 59.00, 120, 'cup.jpg', 4),
('三只松鼠坚果礼盒', '每日坚果混合装750g，7种坚果搭配，健康零食', 99.00, 90, 'nuts.jpg', 5);

-- ============================================
-- 购物车表
-- ============================================
DROP TABLE IF EXISTS `cart_items`;
CREATE TABLE `cart_items` (
    `id`         INT AUTO_INCREMENT PRIMARY KEY COMMENT '购物车ID',
    `user_id`    INT NOT NULL COMMENT '用户ID',
    `product_id` INT NOT NULL COMMENT '商品ID',
    `quantity`   INT DEFAULT 1 COMMENT '数量',
    `add_time`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
    FOREIGN KEY (`user_id`)    REFERENCES `users`(`id`),
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- ============================================
-- 订单表
-- ============================================
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
    `id`          INT AUTO_INCREMENT PRIMARY KEY COMMENT '订单ID',
    `order_no`    VARCHAR(32)  NOT NULL UNIQUE COMMENT '订单编号',
    `user_id`     INT          NOT NULL  COMMENT '用户ID',
    `total_price` DECIMAL(10,2) NOT NULL COMMENT '总金额',
    `status`      VARCHAR(20)  DEFAULT 'pending' COMMENT '状态：pending-待付款，paid-已付款，shipped-已发货，completed-已完成，canceled-已取消',
    `address`     VARCHAR(255) DEFAULT NULL COMMENT '收货地址',
    `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- ============================================
-- 订单详情表
-- ============================================
DROP TABLE IF EXISTS `order_items`;
CREATE TABLE `order_items` (
    `id`           INT AUTO_INCREMENT PRIMARY KEY COMMENT '明细ID',
    `order_id`     INT          NOT NULL COMMENT '订单ID',
    `product_id`   INT          DEFAULT NULL COMMENT '商品ID',
    `product_name` VARCHAR(100) NOT NULL COMMENT '商品名称（快照）',
    `product_price` DECIMAL(10,2) NOT NULL COMMENT '下单时单价',
    `quantity`     INT          NOT NULL COMMENT '数量',
    FOREIGN KEY (`order_id`)   REFERENCES `orders`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单详情表';
