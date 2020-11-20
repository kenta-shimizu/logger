@echo off
cd /d %~dp0

rem Simple

set PATH_BIN="./../Logger.jar"
set MAIN_CLASS=com.shimizukenta.logger.tcpiplogger.SimpleTcpIpLogger

set PATH_LOG=--path ./log/log.log
set CONNECT=--connect 127.0.0.1:23000

set ADD_TIMESTAMP=--add-line-timestamp true

set ISECHO=--echo true


java -cp %PATH_BIN% %MAIN_CLASS% ^
%CONNECT% %PATH_LOG% ^
%ADD_TIMESTAMP% ^
%ISECHO%

exit 0