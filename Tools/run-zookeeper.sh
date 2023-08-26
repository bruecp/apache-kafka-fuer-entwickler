#!/bin/sh

. ./set-environment.sh

export KAFKA_OPTS=-Dzookeeper.admin.enableServer=false

cd kafka

rm -rf data
rm -rf logs

bin/zookeeper-server-start.sh \
	$ZOOKEEPER_PORT \
	data/zookeeper