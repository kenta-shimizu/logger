#!/bin/sh

path_bin="./../Logger.jar"
main_class="testserver.TestTcpIpServer"

java -cp ${path_bin} ${main_class}

exit 0
