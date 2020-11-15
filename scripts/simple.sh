#!/bin/sh

#path_jar="/path/to/jar.jar"
path_jar="./../Export.jar"

main_class="com.shimizukenta.logger.tcpiplogger.SimpleTcpIpLogger"

#path_log="--path /path/to/log/log.log"
path_log="--path ./../log/log.log"

connect="--connect 127.0.0.1:23000"

add_timestamp="--add-line-timestamp true"


java -cp ${path_jar} ${main_class} ${connect} ${path_log} ${add_timestamp}

exit 0

