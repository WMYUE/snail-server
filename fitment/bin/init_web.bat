@echo off


echo [INFO]编译全部模块

cd ..

call 
mvn clean install -Dmaven.test.skip=true -Pweb

cd blueprint-admin-web

del /Q webapp\WEB-INF\lib\*.*

call 
mvn  dependency:copy-dependencies -DoutputDirectory=webapp/WEB-INF/lib - DincludeScope=runtime -Dsilent=true -DexcludeGroupIds=jspapi,javax.servlet


echo.

echo.

echo [INFO] 编译完成

echo.

pause
