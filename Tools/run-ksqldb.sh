#!/bin/sh

. ./set-environment.sh

export KSQL_OPTS="-Dbootstrap.servers=localhost:$KAFKA_PORT -Dlisteners=http://0.0.0.0:$KSQLDB_PORT"

cd ksqldb

bin/ksql-server-start \
	etc/ksqldb/ksql-server.properties