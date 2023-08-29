@echo off

title Kafka

call set-environment

pushd kafka

call bin\windows\kafka-server-start ^
	config\server.properties ^
	--override zookeeper.connect=localhost:%ZOOKEEPER_PORT% ^
	--override listeners=PLAINTEXT://:%KAFKA_PORT% ^
	--override log.dirs=data\kafka

popd

pause