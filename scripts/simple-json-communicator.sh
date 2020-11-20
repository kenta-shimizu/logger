#!/bin/sh

#path_jar="/path/to/jar.jar"
path_jar="./../Logger.jar"

main_class="com.shimizukenta.logger.jsoncommunicatorlogger.SimpleJsonCommunicatorLogger"

path_full="--path-full-timestamp './log_'uuuuMMdd'/log_'uuuuMMddHHmmssSS'.json'"
#path_prefix="--path-prefix-timestamp ./log/log_"
#path_suffix="--path-suffix-timestamp .json"
#path_timestamp="--path-timestamp-format uuuuMMddHHmmssSS"

connect="--connect 127.0.0.1:10000"

#echo="--echo false"


java -cp ${path_jar} ${main_class} \
${connect} \
${path_full} ${path_prefix} ${path_suffix} ${path_timestamp} \
${echo}

exit 0
