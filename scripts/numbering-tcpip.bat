@echo off
cd /d %~dp0

rem Numbering

set PATH_BIN="./../Logger.jar"
set MAIN_CLASS=com.shimizukenta.logger.tcpiplogger.NumberingLineLimitTcpIpLogger

set CONNECT=--connect 127.0.0.1:23000

set LINE_LIMIT=--line-limit 9999

set ADD_TIMESTAMP=--add-line-timestamp true

set ISECHO=--echo true

set PATH_FULL=--path-full-numbering ./log_%%04d/log_%%04d.log
set PATH_PREFIX=--path-prefix-numbering ./log/log_
set PATH_SUFFIX=--path-suffix-numbering .log
set PATH_TIMESTAMP=--path-numbering-format %%06d

java -cp %PATH_BIN% %MAIN_CLASS% ^
%CONNECT% ^
%LINE_LIMIT% ^
%ADD_TIMESTAMP% ^
%PATH_FULL% %PATH_PREFIX% %PATH_SUFFIX% %PATH_TIMESTAMP% ^
%ISECHO%

exit 0
