@echo off
cd /d %~dp0

rem TestJsonCommunicatorServer

set PATH_BIN="./../Logger.jar"
set MAIN_CLASS=testserver.TestJsonCommunicatorServer

java -cp %PATH_BIN% %MAIN_CLASS%

exit 0