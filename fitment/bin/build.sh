cd ..
git pull

mvn clean install -Dmaven.test.skip=true -Pweb
cd fitment-webserver
rm -f webapp/WEB-INF/lib/*
mvn dependency:copy-dependencies -DoutputDirectory=webapp/WEB-INF/lib -DincludeScope=runtime -Dsilent=true -DexcludeGroupIds=jspapi,javax.servlet
