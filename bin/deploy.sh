#!/bin/bash
cd ../blueprint-admin-web
rm -rf /opt/lanxin/webapps/blueprint/WEB-INF/lib/*
rm -rf /opt/lanxin/webapps/blueprint/WEB-INF/classes/com/*
cp -rf webapp/WEB-INF/lib/* /opt/lanxin/webapps/blueprint/WEB-INF/lib/
cp -rf webapp/WEB-INF/classes/com/* /opt/lanxin/webapps/blueprint/WEB-INF/classes/com/
