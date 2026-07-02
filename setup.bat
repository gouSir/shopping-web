@echo off
chcp 65001 >nul
echo ============================================
echo   简易购物网站 - 数据库一键导入脚本
echo ============================================
echo.

set /p MYSQL_PWD=请输入你的 MySQL root 密码:
echo.

echo [1/3] 正在连接 MySQL...
mysql -u root -p%MYSQL_PWD% -e "SELECT 1" 2>nul
if errorlevel 1 (
    echo [错误] 无法连接 MySQL，请检查密码是否正确
    pause
    exit /b 1
)
echo [OK] MySQL 连接成功！

echo [2/3] 正在导入数据库...
mysql -u root -p%MYSQL_PWD% < "%~dp0sql\shop.sql"
if errorlevel 1 (
    echo [错误] 数据库导入失败
    pause
    exit /b 1
)
echo [OK] 数据库导入成功！

echo [3/3] 正在更新配置文件...
> "%~dp0src\main\resources\db.properties" (
    echo # 数据库连接配置（由导入脚本自动生成）
    echo db.url=jdbc:mysql://localhost:3306/shopping?useSSL=false^&serverTimezone=Asia/Shanghai^&characterEncoding=utf8
    echo db.username=root
    echo db.password=%MYSQL_PWD%
)
echo [OK] 配置文件已更新！

echo.
echo ============================================
echo   全部完成！可以启动项目了
echo   管理员账号: admin / admin123
echo   测试账号:   test  / test123
echo ============================================
pause
