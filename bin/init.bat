@echo off

echo [INFO]编译全部模块
cd ..
call mvn clean install -Dmaven.test.skip=true -Pall -U

echo.
echo.
echo [INFO] 编译完成
echo.

pause
