#!/bin/sh

path_src="src/main/java/com"
path_test="src/test"
path_bin="bin"
path_export_jar="Logger.jar"
version="8"

# remove bin files
rm -Rf ${path_bin}

# mkdir bin
mkdir ${path_bin}

# compile-src
javac -d ${path_bin} \
--release ${version} \
$(find ${path_src} -name "*.java")

# compile-test
#javac -d ${path_bin} \
#--class-path ${path_bin} \
#--release ${version} \
#$(find ${path_test} -name "*.java")


# jar
jar -c \
-f ${path_export_jar} \
-C ${path_bin} .

