@echo off
cd /d %~dp0

rem Timestamp

set PATH_BIN="./../Logger.jar"
set MAIN_CLASS=com.shimizukenta.logger.jsoncommunicatorlogger.SimpleJsonCommunicatorLogger

set CONNECT=--connect 127.0.0.1:10000

set ISECHO=--echo true

set PATH_FULL=--path-full-timestamp './log_'uuuuMMdd'/log_'uuuuMMddHHmmssSS'.json'
set PATH_PREFIX=--path-prefix-timestamp ./log/log_
set PATH_SUFFIX=--path-suffix-timestamp .json
set PATH_TIMESTAMP=--path-timestamp-format uuuuMMddHHmmssSSS

java -cp %PATH_BIN% %MAIN_CLASS% ^
%CONNECT% ^
%PATH_FULL% %PATH_PREFIX% %PATH_SUFFIX% %PATH_TIMESTAMP% ^
%ISECHO%

exit 0
