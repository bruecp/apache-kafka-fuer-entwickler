@echo off

title Zookeeper

call set-environment

set KAFKA_OPTS=-Dzookeeper.admin.enableServer=false

pushd kafka

rmdir /s /q data 2>nul
rmdir /s /q logs 2>nul

call bin\windows\zookeeper-server-start ^
	%ZOOKEEPER_PORT% ^
	data\zookeeper

popd

pause