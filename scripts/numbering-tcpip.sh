#!/bin/sh

#path_jar="/path/to/jar.jar"
path_jar="./../Logger.jar"

main_class="com.shimizukenta.logger.tcpiplogger.NumberingLineLimitTcpIpLogger"

path_full="--path-full-numbering ./log_%04d/log_%04d.log"
#path_prefix="--path-prefix-numbering ./log/log_"
#path_suffix="--path-suffix-numbering .log"
#path_timestamp="--path-numbering-format %04d"

connect="--connect 127.0.0.1:23000"

line_limit="--line-limit 9999"

#add_timestamp="--add-line-timestamp true"

#echo="--echo false"


java -cp ${path_jar} ${main_class} \
${connect} \
${add_timestamp} ${line_limit} \
${path_full} ${path_prefix} ${path_suffix} ${path_timestamp} \
${echo}

exit 0
