cd ..
git pull
mvn package
mkdir -p release/config
cp target/*.jar release/
cp src/main/resources/application.properties release/config/application.properties.template
