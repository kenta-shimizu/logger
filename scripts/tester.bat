@echo off
cd /d %~dp0

rem TestServer

set PATH_BIN="./../exports/TestServer.jar"
set MAIN_CLASS=testserver.TestServer

java -cp %PATH_BIN% %MAIN_CLASS%

exit 0