call cls
call mvn6 clean install -Dmaven.test.skip=true
call rmdir /S /Q "C:\Documents and Settings\jacob.robertson\.hudson\plugins\security-no-captcha"
call copy target\security-no-captcha.hpi "C:\Documents and Settings\jacob.robertson\.hudson\plugins"
call java -jar C:\softwaredistribution\workspace\hudson\hudson\main\war/target/hudson.war
rem call java -jar C:\softwaredistribution\hudson\hudson-1.357.war