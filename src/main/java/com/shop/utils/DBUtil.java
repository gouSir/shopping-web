package com.shop.utils;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * 数据库工具类 - 管理数据库连接
 *
 * 数据库配置位于 src/main/resources/db.properties
 * 换电脑只需修改 db.properties 中的用户名和密码即可
 */
public class DBUtil {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static String dbUrl;
    private static String user;
    private static String password;

    // 是否已加载驱动
    private static boolean driverLoaded = false;

    /**
     * 静态加载 JDBC 驱动和数据库配置
     */
    static {
        // 1. 加载配置文件
        try (InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new RuntimeException("找不到 db.properties 配置文件，请检查 src/main/resources/ 目录");
            }
            Properties props = new Properties();
            props.load(in);
            dbUrl    = props.getProperty("db.url");
            user     = props.getProperty("db.username");
            password = props.getProperty("db.password");

            if (dbUrl == null || user == null || password == null) {
                throw new RuntimeException("db.properties 配置不完整，请检查 db.url / db.username / db.password");
            }
        } catch (Exception e) {
            System.err.println("!!! 数据库配置加载失败: " + e.getMessage());
        }

        // 2. 加载驱动
        try {
            Class.forName(DRIVER);
            driverLoaded = true;
        } catch (ClassNotFoundException e) {
            System.err.println("!!! MySQL驱动加载失败，请检查 pom.xml 中 mysql-connector 依赖: " + e.getMessage());
            driverLoaded = false;
        }
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection() throws SQLException {
        if (!driverLoaded) {
            throw new SQLException("数据库驱动未加载，无法获取连接。");
        }
        return DriverManager.getConnection(dbUrl, user, password);
    }

    /**
     * 安全关闭 Connection / Statement / ResultSet
     */
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null)   rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重载：关闭 Connection 和 Statement（无 ResultSet 时使用）
     */
    public static void close(Connection conn, Statement stmt) {
        close(conn, stmt, null);
    }

    /**
     * 回滚事务
     */
    public static void rollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
