#!/bin/sh

path_bin="./../exports/TestServer.jar"
main_class="testserver.TestServer"

java -cp ${path_bin} ${main_class}

exit 0
