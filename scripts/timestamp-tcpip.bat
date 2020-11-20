@echo off
cd /d %~dp0

rem Timestamp

set PATH_BIN="./../Logger.jar"
set MAIN_CLASS=com.shimizukenta.logger.tcpiplogger.TimestampLineLimitTcpIpLogger

set CONNECT=--connect 127.0.0.1:23000

set LINE_LIMIT=--line-limit 9999

set ADD_TIMESTAMP=--add-line-timestamp true

set ISECHO=--echo true

set PATH_FULL=--path-full-timestamp './log_'uuuuMMdd'/log_'uuuuMMddHHmmssSS'.log'
set PATH_PREFIX=--path-prefix-timestamp ./log/log_
set PATH_SUFFIX=--path-suffix-timestamp .log
set PATH_TIMESTAMP=--path-timestamp-format uuuuMMddHHmmssSSS

java -cp %PATH_BIN% %MAIN_CLASS% ^
%CONNECT% ^
%LINE_LIMIT% ^
%ADD_TIMESTAMP% ^
%PATH_FULL% %PATH_PREFIX% %PATH_SUFFIX% %PATH_TIMESTAMP% ^
%ISECHO%

exit 0
