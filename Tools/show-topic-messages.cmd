@echo off

title Kafka Console Consumer

call set-environment

echo Select topic: 
echo [1] iot.measurements.native
echo [2] iot.measurements.filtered
echo [3] newsletter.readers
choice /c:123 /n

if errorlevel 3 set TOPIC=newsletter.readers
if errorlevel 2 set TOPIC=iot.measurements.filtered
if errorlevel 1 set TOPIC=iot.measurements.native

pushd kafka

call bin\windows\kafka-console-consumer ^
	--bootstrap-server localhost:%KAFKA_PORT% ^
	--topic %TOPIC% ^
	--from-beginning

popd

pause