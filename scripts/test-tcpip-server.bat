@echo off
cd /d %~dp0

rem TestTcpIpServer

set PATH_BIN="./../Export.jar"
set MAIN_CLASS=testserver.TestTcpIpServer

java -cp %PATH_BIN% %MAIN_CLASS%

exit 0