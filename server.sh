#!/bin/bash
cd ~/Documents/programming/edgeville/edgeville_server/
while true
do
java -cp ./bin_release:./data/deps/bzip2.jar:./data/deps/commons-codec-1.10.jar:./data/deps/commons-compress-1.14.jar:./data/deps/gson-2.3.1.jar:./data/deps/guava-18.0.jar:./data/deps/HikariCP-2.4.7.jar:./data/deps/mysql-connector-java-5.1.39.jar:./data/deps/netty-all-4.1.5.Final.jar:./data/deps/slf4j-api-1.7.21.jar: net.edge.Server true
done