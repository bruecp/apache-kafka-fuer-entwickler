@echo off

title ksqlDB

call set-environment

pushd ksqldb

java ^
	-cp share\java\ksqldb\* ^
	-Dbootstrap.servers=localhost:%KAFKA_PORT% ^
	-Dlisteners=http://0.0.0.0:%KSQLDB_PORT% ^
	io.confluent.ksql.rest.server.KsqlServerMain ^
	etc\ksqldb\ksql-server.properties

popd

pause