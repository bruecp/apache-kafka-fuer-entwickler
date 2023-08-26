#!/bin/sh

. ./set-environment.sh

cd kafdrop

java -jar kafdrop-3.31.0.jar \
	--kafka.brokerConnect=localhost:$KAFKA_PORT \
	--server.port=$KAFDROP_PORT \
	--management.server.port=$KAFDROP_PORT