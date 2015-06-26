#!/bin/sh
git pull origin
git checkout staging
java -Dfile.encoding=UTF-8 -Duser.timezone=GMT  -Djava.net.preferIPv6Stack=true -Xmx7000M -jar ./QualoscopyWebServer.jar 
