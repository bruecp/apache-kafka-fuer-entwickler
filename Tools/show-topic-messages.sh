#!/bin/sh

. ./set-environment.sh

PS3=
echo Select topic:
select TOPIC in iot.measurements.native iot.measurements.filtered newsletter.readers
do 
	if [ $REPLY == 1 ] || [ $REPLY == 2 ] || [ $REPLY == 3 ]; then
		break
	fi
done

cd kafka

bin/kafka-console-consumer.sh \
	--bootstrap-server localhost:$KAFKA_PORT \
	--topic $TOPIC \
	--from-beginning