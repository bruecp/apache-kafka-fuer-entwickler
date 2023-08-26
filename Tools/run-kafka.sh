#!/bin/sh

. ./set-environment.sh

cd kafka

bin/kafka-server-start.sh \
	config/server.properties \
	--override zookeeper.connect=localhost:$ZOOKEEPER_PORT \
	--override listeners=PLAINTEXT://:$KAFKA_PORT \
	--override log.dirs=data/kafka