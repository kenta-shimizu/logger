#!/bin/sh

#path_jar="/path/to/jar.jar"
path_jar="./../Export.jar"

main_class="com.shimizukenta.logger.tcpiplogger.TimestampLineLimitTcpIpLogger"

path_full="--path-full-timestamp './log_'yyyyMMdd'/log_'yyyyMMddHHmmssSS'.log'"
#path_prefix="--path-prefix-timestamp ./log/log_"
#path_suffix="--path-suffix-timestamp .log"
#path_timestamp="--path-timestamp-format yyyyMMddHHmmssSSS"

connect="--connect 127.0.0.1:23000"

line_limit="--line-limit 25"

add_timestamp="--add-line-timestamp true"

#echo="--echo false"


java -cp ${path_jar} ${main_class} \
${connect} ${path_log} \
${add_timestamp} ${line_limit} \
${path_full} ${path_prefix} ${path_suffix} ${path_timestamp} \
${echo}

exit 0
