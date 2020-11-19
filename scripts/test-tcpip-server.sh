#!/bin/sh

path_bin="./../Export.jar"
main_class="testserver.TestTcpIpServer"

java -cp ${path_bin} ${main_class}

exit 0
