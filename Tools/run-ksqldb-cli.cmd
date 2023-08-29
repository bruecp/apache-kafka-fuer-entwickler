@echo off

title ksqlDB CLI

call set-environment

pushd ksqldb

java ^
	-cp share\java\ksqldb\* ^
	io.confluent.ksql.Ksql ^
	http://0.0.0.0:%KSQLDB_PORT%

popd

pause