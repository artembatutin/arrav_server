#!/bin/bash
cd ~/Documents/programming/edgeville/edgeville_server/
while true
do
java -cp ./bin_release:./data/deps/json-simple-1.1.1.jar:./data/deps/bzip2.jar:./data/deps/commons-codec-1.10.jar:./data/deps/commons-compress-1.14.jar:./data/deps/gson-2.8.0.jar:./data/deps/guava-22.0.jar:./data/deps/HikariCP-2.6.2.jar:./data/deps/mysql-connector-java-6.0.6.jar:./data/deps/netty-all-4.1.5.Final.jar:./data/deps/slf4j-api-1.7.25.jar: net.edge.Server true
done