@echo off
title file-server
java -server -Xmx2024m -cp bin; deps/netty-3.5.8.Final.jar FileServer
pause