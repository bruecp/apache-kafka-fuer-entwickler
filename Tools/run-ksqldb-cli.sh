#!/bin/sh

. ./set-environment.sh

cd ksqldb

bin/ksql \
	http://0.0.0.0:$KSQLDB_PORT