# 🛒 简易购物网站 — Java Web 期末作业

一个基于 **Servlet + JSP + MySQL** 的简易购物网站。

## 技术栈

| 技术 | 说明 |
|------|------|
| Java 8+ | 后端开发语言 |
| Servlet 4.0 | Web 控制器 |
| JSP 2.3 + JSTL | 前端视图模板 |
| MySQL 8.0 | 关系型数据库 |
| Maven 3.x | 项目构建与依赖管理 |
| Tomcat 9 | Web 容器 |

## 功能模块

### 用户端
- ✅ 用户注册 / 登录 / 退出
- ✅ 商品列表浏览（分页 + 分类筛选 + 关键词搜索）
- ✅ 商品详情查看
- ✅ 购物车（添加、修改数量、删除）
- ✅ 下单结算（扣库存、清空购物车）
- ✅ 订单查看 / 订单详情

### 管理端
- ✅ 商品管理（增删改查）
- ✅ 订单管理（查看所有订单、修改状态）

## 快速开始

### 1. 初始化数据库

```bash
# 在 MySQL 中执行
mysql -u root -p < sql/shop.sql
```

> 默认账号：admin / admin123（管理员），test / test123（普通用户）

### 2. 修改数据库连接

编辑 `src/main/java/com/shop/utils/DBUtil.java` 中的连接信息：

```java
private static final String DB_URL   = "jdbc:mysql://localhost:3306/shopping?...";
private static final String USER     = "root";    // 改成你的用户名
private static final String PASSWORD = "root";    // 改成你的密码
```

### 3. 编译运行

```bash
cd ShoppingWeb

# 方式一：Maven Tomcat 插件运行
mvn clean tomcat7:run

# 方式二：打包 war 部署到 Tomcat
mvn clean package
# 将 target/ShoppingWeb.war 复制到 Tomcat 的 webapps 目录
```

### 4. 访问网站

打开浏览器访问：**http://localhost:8080/ShoppingWeb**

## 项目结构

```
ShoppingWeb/
├── pom.xml                      # Maven 配置
├── sql/shop.sql                 # 数据库初始化脚本
├── src/main/java/com/shop/
│   ├── model/                   # 实体类
│   │   ├── User.java            #   用户
│   │   ├── Product.java         #   商品
│   │   ├── Category.java        #   分类
│   │   ├── CartItem.java        #   购物车项
│   │   └── Order.java           #   订单 + 订单明细
│   ├── dao/                     # 数据访问层
│   │   ├── UserDao.java         #   用户 DAO
│   │   ├── ProductDao.java      #   商品 DAO
│   │   ├── CartDao.java         #   购物车 DAO
│   │   └── OrderDao.java        #   订单 DAO
│   ├── servlet/                 # Servlet 控制器
│   │   ├── LoginServlet.java    #   登录
│   │   ├── RegisterServlet.java #   注册
│   │   ├── LogoutServlet.java   #   退出
│   │   ├── ProductServlet.java  #   商品列表/详情
│   │   ├── CartServlet.java     #   购物车
│   │   ├── OrderServlet.java    #   下单/订单
│   │   └── AdminServlet.java    #   后台管理
│   ├── filter/                  # 过滤器
│   │   ├── EncodingFilter.java  #   编码过滤器
│   │   ├── LoginFilter.java     #   登录拦截
│   │   └── AdminFilter.java     #   管理员拦截
│   └── utils/
│       └── DBUtil.java          # 数据库工具类
└── src/main/webapp/
    ├── WEB-INF/web.xml          # Web 部署描述符
    ├── header.jsp / footer.jsp  # 公共头部/底部
    ├── index.jsp                # 首页
    ├── login.jsp                # 登录页
    ├── register.jsp             # 注册页
    ├── products.jsp             # 商品列表页
    ├── product_detail.jsp       # 商品详情页
    ├── cart.jsp                 # 购物车页
    ├── orders.jsp               # 订单列表页
    ├── order_detail.jsp         # 订单详情页
    ├── admin/                   # 管理后台页面
    ├── error/                   # 错误页面
    ├── css/style.css            # 统一样式
    └── js/main.js               # 全局脚本
```

## 预设账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |
| 普通用户 | test | test123 |

## 注意事项

- 商品图片默认读取 `images/` 目录下的图片文件
- 如果没有图片文件，页面会自动使用默认占位图
- 需要先启动 MySQL 数据库服务，再启动 Web 应用
- 建议使用 IntelliJ IDEA 或 Eclipse EE 导入项目
