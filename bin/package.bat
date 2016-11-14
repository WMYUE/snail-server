cd ..
call mvn package
cp target/*.jar release/lib
cp src/main/resources/application.properties release/conf/application.properties.template
cp src/main/resources/log4j.properties release/conf/log4j.properties.template